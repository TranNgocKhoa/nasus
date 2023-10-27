package io.github.tranngockhoa.nasus.bean;

import io.github.tranngockhoa.nasus.IoC;
import io.github.tranngockhoa.nasus.bean.wire.SmartPhone;

public class Application {
    public static void main(String[] args) {
        IoC ioC = IoC.initBeans(Application.class);

        SmartPhone smartPhone = ioC.getBean(SmartPhone.class);
    }
}
