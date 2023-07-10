package com.kanban.kanban;

import com.kanban.kanban.filter.UserFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class KanbanApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanbanApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean filterUrl() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new UserFilter());

        filterRegistrationBean.addUrlPatterns("/api/v1/user/details", "/api/v1/user/addProject/*", "/api/v1/user/removeProject/*", "/api/v1/user/projectList");
        return filterRegistrationBean;
    }

}
