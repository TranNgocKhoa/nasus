package io.github.tranngockhoa.nasus;


import io.github.tranngockhoa.nasus.api.BeanContainer;
import io.github.tranngockhoa.nasus.exception.IoCException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BeanContainerImpl implements BeanContainer {
    private final Map<Class<?>, Map<String, Object>> beans = new HashMap<>();

    @Override
    public void putBean(Class<?> clazz, Object instance) {
        this.putBean(clazz, instance, clazz.getName());
    }

    private void putBean(Class<?> clazz, Object instance, String name) {
        Map<String, Object> beansByClassMap = beans.computeIfAbsent(clazz, key -> new HashMap<>());
        if (beansByClassMap.get(name) == null) {
            beansByClassMap.put(name, instance);
        } else {
            throw new IoCException("Bean with type " + clazz + " and name " + name + " is already registered.");
        }
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return this.getBean(clazz, clazz.getName());
    }

    private <T> T getBean(Class<T> clazz, String name) {
        Map<String, Object> beanByClassMap = beans.get(clazz);

        if (beanByClassMap == null || beanByClassMap.isEmpty()) {
            throw new IoCException("Bean not found for class " + clazz.getName());
        }

        if (beanByClassMap.size() == 1) {
            return this.getBeanExtractType(clazz, beanByClassMap.values().iterator().next());
        }

        Object bean = beanByClassMap.get(name);
        if (bean == null) {
            String errorMessage = "There are " + beanByClassMap.size() + " of bean " + name
                    + " Expected single implementation or use @Qualifier to resolve conflict";
            throw new IoCException(errorMessage);
        }

        return this.getBeanExtractType(clazz, bean);
    }

    @Override
    public <T> boolean containsBean(Class<T> clazz) {
        return Optional.ofNullable(beans.get(clazz))
                .filter(beanByClassMap -> !beanByClassMap.isEmpty())
                .isPresent();
    }

    public int size() {
        return beans.size();
    }

    private <T> T getBeanExtractType(Class<T> clazz, Object bean) {
        return Optional.ofNullable(bean)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .orElseThrow(() -> new IoCException("Bean not found for class " + clazz.getName()));
    }
}
