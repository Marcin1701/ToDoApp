package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

// Klasa konfiguracyjna, żeby zadziałał Interceptors
// Tutaj możemy wstrzykiwać interceptory jako beany
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    // Wszystkie możliwe interceptory
    private Set<HandlerInterceptor> interceptors;

    public MvcConfiguration(final Set<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    // Dodawanie nowych interceptorów
    // W łąńcuchach filtrów - spring najpierw honoruje JEE czyli LoggerFilter, potem Interceptor
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // Dodajemy od razu wszystkie interceptory
        interceptors.forEach(registry::addInterceptor);
    }
}
