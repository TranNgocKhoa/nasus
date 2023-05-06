package io.github.tranngockhoa.nasus;

import io.github.tranngockhoa.nasus.api.annotation.Bean;
import io.github.tranngockhoa.nasus.api.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImplementationRegister {
    private static ImplementationRegister INSTANCE;

    public static ImplementationRegister getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        synchronized (ImplementationRegister.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new ImplementationRegister();

            return INSTANCE;
        }
    }

    private ImplementationRegister() {
    }

    public void register(List<Class<?>> classesInPackage, ImplementationContainer implementationContainer) {
        List<Class<?>> configurationClasses = classesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toList());
        this.registerImplementationForBeansInConfigurationClasses(configurationClasses, implementationContainer);
    }

    private void registerImplementationForBeansInConfigurationClasses(List<Class<?>> configurationClasses, ImplementationContainer implementationContainer) {
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
