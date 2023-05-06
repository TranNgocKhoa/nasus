package io.github.tranngockhoa.nasus.api;

public interface BeanContainer {
    void putBean(Class<?> clazz, Object instance);

    <T> T getBean(Class<T> clazz);

    <T> boolean containsBean(Class<T> clazz);
}
