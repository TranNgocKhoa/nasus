package io.github.tranngockhoa.nasus.bean.wire;

import io.github.tranngockhoa.nasus.api.annotation.Bean;
import io.github.tranngockhoa.nasus.api.annotation.Configuration;

@Configuration
public class ConfigBean {

    @Bean
    public Wire wire() {
        return new Wire();
    }

    @Bean
    public Connector connector(Wire wire) {
        return new Connector(wire);
    }

    @Bean
    public Screen screen() {
        return new Screen();
    }

    @Bean
    public Battery battery() {
        return new Battery();
    }

    @Bean
    public SoC soC() {
        return new SoC();
    }

    @Bean
    public Adapter adapter() {
        return new Adapter();
    }

    @Bean
    public SmartPhone smartPhone(Screen screen, Battery battery, SoC soC, Connector connector) {
        return new SmartPhone(screen, battery, soC, connector);
    }

    @Bean
    public SmartPhoneWithAdapter smartPhoneWithAdapter(SmartPhone smartPhone, Adapter adapter) {
        return new SmartPhoneWithAdapter(smartPhone, adapter);
    }


}
