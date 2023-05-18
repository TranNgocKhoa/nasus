package io.github.tranngockhoa.nasus;

import io.github.tranngockhoa.nasus.api.BeanContainer;
import io.github.tranngockhoa.nasus.api.annotation.Bean;
import io.github.tranngockhoa.nasus.api.annotation.Configuration;
import io.github.tranngockhoa.nasus.exception.InitBeanMethodException;
import io.github.tranngockhoa.nasus.exception.IoCException;
import io.github.tranngockhoa.nasus.loader.NasusClassLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class IoC implements BeanContainer {
    private static BeanContainer CONTEXT;
    private final BeanContainer beanContainer = new BeanContainerImpl();
    private final ImplementationContainer implementationContainer = new ImplementationContainerImpl();

    public static IoC initBeans(Class<?> mainClass) {
        IoC ioC = new IoC();
        CONTEXT = ioC.beanContainer;
        long start = System.currentTimeMillis();
        ioC.doInitBeans(mainClass);
        long end = System.currentTimeMillis();

        System.out.println("Take: " + (end - start) + "ms");
        System.out.println("Size: " + ((BeanContainerImpl) ioC.beanContainer).size() + " instances");

        return ioC;
    }

    public static BeanContainer context() {
        return CONTEXT;
    }

    @Override
    public void putBean(Class<?> clazz, Object instance) {
        beanContainer.putBean(clazz, instance);
    }

    /**
     * Get bean by Class
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> T getBean(Class<T> clazz) {
        return beanContainer.getBean(clazz);
    }

    @Override
    public <T> boolean containsBean(Class<T> clazz) {
        return false;
    }

    private void doInitBeans(Class<?> mainClass) {
        this.iniBeansFromMainClass(mainClass);
    }

    private void iniBeansFromMainClass(Class<?> mainClass) {
        System.out.println("Init from Main Class " + mainClass.getName());
        this.initBeansInPackage(mainClass.getPackage().getName());
    }

    private void initBeansInPackage(String packageName) {
        beanContainer.putBean(IoC.class, this);
        implementationContainer.put(IoC.class, IoC.class);

        // get classes in package name
        List<Class<?>> classesInPackage = NasusClassLoader.getInstance().getClassesInPackage(packageName);
        // init implementation container by @Bean in @Configuration classes
        ConfigurationClassImplementationRegister.getInstance()
                .register(classesInPackage, implementationContainer);
        // init beans in @Configuration classes
        this.initConfigurationDeclaredBeans(classesInPackage);
    }

    private void initConfigurationDeclaredBeans(List<Class<?>> classesInPackage) {
        Deque<Class<?>> configurationClassQueue = new ArrayDeque<>();
        classesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Configuration.class))
                .forEach(configurationClassQueue::add);

        while (!configurationClassQueue.isEmpty()) {
            // Create configuration object. Considered as a Component
            Class<?> configurationClass = configurationClassQueue.poll();
            try {
                System.out.println("Init from config class " + configurationClass.getName());
                this.tryInitBeanConfigurationClass(configurationClass);
            } catch (IoCException e) {
                configurationClassQueue.addLast(configurationClass);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void tryInitBeanConfigurationClass(Class<?> configurationClass) throws IllegalAccessException, InvocationTargetException {
        System.out.println("Init " + configurationClass.getName());
        _initBean(configurationClass);

        // Init fields in Configuration object
        Object configurationObject = beanContainer.getBean(configurationClass);

        Deque<Method> beanMethodQueue = new ArrayDeque<>();
        Arrays.stream(configurationClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(beanMethodQueue::add);

        int totalMethods = beanMethodQueue.size();
        int count = 0;
        while (!beanMethodQueue.isEmpty()) {
            Method beanMethod = beanMethodQueue.poll();
            try {
                System.out.println("Init method " + beanMethod.getName());
                this._initBeanMethod(configurationObject, beanMethod);
            } catch (InitBeanMethodException e) {
                System.err.println("Error " + e.getMessage());
                count++;
                beanMethodQueue.addLast(beanMethod);

                if (count > totalMethods) {
                    System.err.println("Error: Can't init some @Bean method at the moment.");
                    throw new IoCException("Can't init some @Bean method at the moment.");
                }
            }
        }
    }

    private void _initBeanMethod(Object configurationObject, Method beanAnnotatedMethod) throws IllegalAccessException, InvocationTargetException {
        Class<?> beanType = beanAnnotatedMethod.getReturnType();
        Parameter[] parameters = beanAnnotatedMethod.getParameters();

        if (beanContainer.containsBean(beanAnnotatedMethod.getReturnType())) {
            return;
        }

        if (parameters.length == 0) {
            Object beanInstance = beanAnnotatedMethod.invoke(configurationObject);
            beanContainer.putBean(beanType, beanInstance);
        } else {
            for (Parameter parameter : parameters) {
                if (!beanContainer.containsBean(parameter.getType())) {
                    throw new InitBeanMethodException("No beanMethod found for " + parameter.getName());
                }
//                this._initBean(parameter.getType());
            }
            Object[] parameterObjects = Arrays.stream(parameters)
                    .map(Parameter::getType)
                    .map(this::getBean)
                    .toArray(Object[]::new);

            Object beanInstance = beanAnnotatedMethod.invoke(configurationObject, parameterObjects);

            beanContainer.putBean(beanType, beanInstance);
        }
    }

    // Init class type instance and put in BeanContainer
    private void _initBean(Class<?> type) {
        Class<?> beanType = implementationContainer.get(type);
        if (beanContainer.containsBean(beanType)) {
            return;
        }

        Constructor<?>[] constructors = beanType.getConstructors();
        this.validateConstructor(beanType, constructors);

        Object instance;
        try {
            Parameter[] parameters = constructors[0].getParameters();
            if (parameters.length == 0) {
                instance = beanType.getConstructor().newInstance();
            } else {
                for (Parameter parameter : parameters) {
                    Class<?> parameterType = parameter.getType();
                    _initBean(parameterType);
                }

                Class<?>[] parameterClasses = Arrays.stream(parameters)
                        .map(Parameter::getType).toArray(Class<?>[]::new);

                Object[] parameterObjects = Arrays.stream(parameterClasses)
                        .map(this::getBean)
                        .toArray(Object[]::new);

                instance = beanType.getConstructor(parameterClasses).newInstance(parameterObjects);
            }
        } catch (Exception e) {
            throw new IoCException("Error when init bean with type = " + beanType);
        }

        beanContainer.putBean(beanType, instance);
    }

    private void validateConstructor(Class<?> beanType, Constructor<?>[] constructors) {
        if (constructors.length == 0) {
            throw new IoCException("There is no public constructor for class " + beanType + ". Invalid to init.");
        }

        if (constructors.length > 1) {
            throw new IoCException("There are " + constructors.length + " constructors for class " + beanType
                    + ". Can't define which one need to be chosen to be init.");
        }
    }
}
