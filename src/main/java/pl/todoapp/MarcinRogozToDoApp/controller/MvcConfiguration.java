package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Klasa konfiguracyjna, żeby zadziałał Interceptors
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    // Dodawanie nowych interceptorów
    // W łąńcuchach filtrów - spring najpierw honoruje JEE czyli LoggerFilter, potem Interceptor
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor());
    }


}
