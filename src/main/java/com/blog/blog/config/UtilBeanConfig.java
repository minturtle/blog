package com.blog.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UtilBeanConfig {

    @Bean
    public RestTemplate rt(){
        return new RestTemplate();
    }
}
