package io.github.tranngockhoa.nasus.bean;

import io.github.tranngockhoa.nasus.IoC;
import io.github.tranngockhoa.nasus.api.BeanContainer;
import io.github.tranngockhoa.nasus.bean.wire.Connector;
import io.github.tranngockhoa.nasus.bean.wire.SmartPhone;
import io.github.tranngockhoa.nasus.bean.wire.SmartPhoneWithAdapter;
import org.junit.jupiter.api.Test;

public class IoCTest {
    static IoC ioC;

    @Test
    void testIocConfiguration() {

        ioC = IoC.initBeans(IoCTest.class);

        SmartPhone bean = ioC.getBean(SmartPhone.class);

        bean.check();
    }

    @Test
    void testMember() {
        ioC = IoC.initBeans(IoCTest.class);

        Connector bean = ioC.getBean(Connector.class);

        bean.check();
    }

    @Test
    void testMultipleSourceBean() {
        ioC = IoC.initBeans(IoCTest.class);

        SmartPhoneWithAdapter bean = ioC.getBean(SmartPhoneWithAdapter.class);

        bean.check();
    }

    @Test
    void checkContext() {
        IoC.initBeans(IoCTest.class);

        BeanContainer context = IoC.context();
        SmartPhoneWithAdapter bean = context.getBean(SmartPhoneWithAdapter.class);

        bean.check();
    }
}
