package pl.todoapp.MarcinRogozToDoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarcinRogozToDoAppApplication {

	// Aby uruchomić budowanie aplikacji:
	// 1. Maven - Package (Lifecycle) albo mvnw.cmd package (w konsoli)
	// 2. Pojawia się folder target

	// Apliacja pozwala nam wystartować Spring
	public static void main(String[] args) {
		// Metoda do uruchomienia Spring
		SpringApplication.run(MarcinRogozToDoAppApplication.class, args);
	}

}
