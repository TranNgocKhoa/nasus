package io.github.tranngockhoa.nasus;

import io.github.tranngockhoa.nasus.api.annotation.Bean;
import io.github.tranngockhoa.nasus.api.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationClassImplementationRegister implements ImplementationRegister {
    private static ImplementationRegister INSTANCE;

    public static ImplementationRegister getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        synchronized (ConfigurationClassImplementationRegister.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new ConfigurationClassImplementationRegister();

            return INSTANCE;
        }
    }

    private ConfigurationClassImplementationRegister() {
    }

    @Override
    public void register(List<Class<?>> classList, ImplementationContainer implementationContainer) {
        List<Class<?>> configurationClasses = classList.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toList());
        this.registerImplementationForBeansMethods(configurationClasses, implementationContainer);
    }

    private void registerImplementationForBeansMethods(List<Class<?>> configurationClasses, ImplementationContainer implementationContainer) {
        for (Class<?> configurationClass : configurationClasses) {
            implementationContainer.put(configurationClass, configurationClass);
            List<Method> beanMethods = Arrays.stream(configurationClass.getMethods())
                    .filter(method -> method.getAnnotation(Bean.class) != null)
                    .toList();

            for (Method method : beanMethods) {
                Class<?> returnType = method.getReturnType();
                implementationContainer.put(returnType, returnType);
            }
        }
    }
}
