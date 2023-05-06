package io.github.tranngockhoa.nasus;

public interface ImplementationContainer {
    void put(Class<?> interfaceClass, Class<?> implementationClass);

    Class<?> get(Class<?> interfaceClass);
}
