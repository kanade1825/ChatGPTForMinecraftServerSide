package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final ApiUserRepository apiUserRepository;

    @Autowired
    public FilterConfig(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyAuthFilter() {
        FilterRegistrationBean<ApiKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ApiKeyAuthFilter(apiUserRepository));
        registrationBean.addUrlPatterns("/json/*");

        return registrationBean;
    }
}
