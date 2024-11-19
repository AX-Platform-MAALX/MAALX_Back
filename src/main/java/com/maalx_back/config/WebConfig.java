package com.maalx_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Allow the frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow necessary HTTP methods
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);// Allow cookies to be sent with requests if needed
    }
}

