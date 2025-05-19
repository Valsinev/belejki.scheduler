package com.belejki.scheduler.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;


@Configuration
public class LocaleConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:locale/messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}




