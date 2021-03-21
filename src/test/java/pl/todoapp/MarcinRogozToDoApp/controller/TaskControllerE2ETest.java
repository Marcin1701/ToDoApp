package pl.todoapp.MarcinRogozToDoApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// Hierarchia testów
// Piramida:
// Najwięcej testów jednostkowych
// Mniej testów integracyjnych - łączą matody z kilkoma klasami
// Testy end to end - jak wstaje aplikacja i co widzi użytkownik

// Aplikacja wstaje na losowym porcie
// Korzystamy z bazy w pamięci
// Używamy profilu a to nie jest dobre, implementacja repo z pamięci
// Ten test nie sprawdza dodania wszystkich metod, czy native query zadziała
// Jeśli używamy własnej implementacji
//@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    // Wczytujemy zaczytany port
    @LocalServerPort    // Wylosowany port wstrzykujemy do zmiennej
    private int port;

    // Klasa odpytuje inne usługi
    // Wykonujemy nim zapytania
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnsAllTasks() {
        // Dane - given
        // Wstanie osobna instancja bazy danych - MAMY TAKĄ SAMĄ BAZĘ CO NA PRODUKCJI
        int initialSize = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        // Kiedy - warunki
        // Wykonujemy połączenie pod zadany port
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        // Potem  test
        assertThat(result).hasSize(initialSize + 2);
    }
}
