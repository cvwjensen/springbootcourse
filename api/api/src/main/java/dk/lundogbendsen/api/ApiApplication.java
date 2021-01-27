package dk.lundogbendsen.api;

import dk.lundogbendsen.api.controller.SecurityFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<SecurityFilter> registrationBean() {
        final FilterRegistrationBean<SecurityFilter> securityFilterFilterRegistrationBean = new FilterRegistrationBean<>(new SecurityFilter());
        securityFilterFilterRegistrationBean.addUrlPatterns("/person/*", "/admin/");
        return securityFilterFilterRegistrationBean;
    }
}
