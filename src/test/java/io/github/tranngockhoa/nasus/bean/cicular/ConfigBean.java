package io.github.tranngockhoa.nasus.bean.cicular;

import io.github.tranngockhoa.nasus.api.annotation.Bean;
import io.github.tranngockhoa.nasus.api.annotation.Configuration;
import io.github.tranngockhoa.nasus.bean.wire.Adapter;
import io.github.tranngockhoa.nasus.bean.wire.Connector;
import io.github.tranngockhoa.nasus.bean.wire.Wire;

@Configuration
public class ConfigBean {

    @Bean
    public Wire wire() {
        return new Wire();
    }

    @Bean
    public Adapter adapter(Wire wire, Connector connector) {
        return new Adapter(wire, connector);
    }

    @Bean
    public Connector connector(Wire wire, Adapter adapter) {
        return new Connector(wire, adapter);
    }

}
