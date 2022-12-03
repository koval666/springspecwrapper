package ru.teadev.springspecwrapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringSpecWrapperAutoConfiguration {

    @Bean
    public BasicSpecifications basicSpecifications() {
        return new BasicSpecificationsImpl();
    }
}
