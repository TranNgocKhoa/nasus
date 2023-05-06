package io.github.tranngockhoa.nasus.bean.wire;

import io.github.tranngockhoa.nasus.api.annotation.Bean;
import io.github.tranngockhoa.nasus.api.annotation.Configuration;

@Configuration
public class ConfigForAdapter {
//    @Bean
    public Adapter adapter() {
        return new Adapter();
    }
}
