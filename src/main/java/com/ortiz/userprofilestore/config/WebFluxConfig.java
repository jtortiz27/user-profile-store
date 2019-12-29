package com.ortiz.userprofilestore.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;

@Configuration
public class WebFluxConfig extends DelegatingWebFluxConfiguration {

    @Override
    protected void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        Jackson2JsonEncoder encoder = new Jackson2JsonEncoder();
        encoder.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        configurer.defaultCodecs().jackson2JsonEncoder(encoder);
    }
}
