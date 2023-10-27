package io.github.tranngockhoa.nasus;

import java.util.function.Supplier;

public class BeanDefinition {
    private Object beanClass;
    private String[] dependsOn;
    private Supplier<?> instanceSupplier;
}
