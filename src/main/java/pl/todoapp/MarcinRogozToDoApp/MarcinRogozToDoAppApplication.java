package pl.todoapp.MarcinRogozToDoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
//import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
//import org.springframework.validation.Validator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

// Doklejamy klasę konfiguracyjną - jeśli jest ona nie w tym samym pakiecie
//@Import(TaskConfigurationProperties.class)
@SpringBootApplication                                  // Do walidacji danych
//@ComponentScan(basePackages = "db.migration")           // Dodatkowe pakiety do skanowania
public class MarcinRogozToDoAppApplication { // implements RepositoryRestConfigurer {

    // Aby uruchomić budowanie aplikacji:
    // 1. Maven - Package (Lifecycle) albo mvnw.cmd package (w konsoli)
    // 2. Pojawia się folder target

    // Apliacja pozwala nam wystartować Spring
    public static void main(String[] args) {
        // Metoda do uruchomienia Spring
        SpringApplication.run(MarcinRogozToDoAppApplication.class, args);
    }

    @Bean
    Validator validator() {
        // Obiekt typu Validator jest klasą zarządzalną przez Springa
        return new LocalValidatorFactoryBean();
    }

    // Parametr validate listener - dodajemy walidator
    // Reaguje na błędy usera podczas wysyłania żądania html
    // Dzięki temu pojawia się informacja z adnotacji @NotBlank w Task
	/*
	@Override
	public void configureValidatingRepositoryEventListener(final ValidatingRepositoryEventListener validatingListener) {
		// Przed utworzeniem
		validatingListener.addValidator("beforeCreate", validator());
		// Przed zapisem
		validatingListener.addValidator("beforeSave", validator());
	}
	*/
}
