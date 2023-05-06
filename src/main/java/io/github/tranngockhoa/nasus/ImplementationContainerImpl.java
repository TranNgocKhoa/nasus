package io.github.tranngockhoa.nasus;

import io.github.tranngockhoa.nasus.exception.IoCException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ImplementationContainerImpl implements ImplementationContainer {
    private final Map<Class<?>, Set<Class<?>>> interfaceImplementationsMap = new HashMap<>();

    @Override
    public void put(Class<?> interfaceClass, Class<?> implementationClass) {
        Set<Class<?>> interfaceClassList = interfaceImplementationsMap.computeIfAbsent(interfaceClass, k -> new HashSet<>());
        interfaceClassList.add(implementationClass);
    }

    @Override
    public Class<?> get(Class<?> interfaceClass) {
        Set<Class<?>> implementationClassSet = interfaceImplementationsMap.get(interfaceClass);

        if (implementationClassSet == null || implementationClassSet.isEmpty()) {
            throw new IoCException("No implementation found for interface " + interfaceClass.getName());
        }

        if (implementationClassSet.size() == 1) {
            return implementationClassSet.iterator().next();
        }

        return implementationClassSet.stream()
                .findFirst()
                .orElseThrow(() -> new IoCException("There are " + implementationClassSet.size()
                        + " of interface " + interfaceClass.getName()));
    }
}
