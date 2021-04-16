package pl.todoapp.MarcinRogozToDoApp.controller;

// Testy integracyjne

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import java.time.LocalDateTime;

// Bez web enviroment - domyślnie jest wstawiany mock
// Nie dostaniemy się do aplikacji
// Są inne metody żeby to zrobić
// Pozwala na to @AutoConfigureMockMvc
// Odpytanie pozostaje bez powstawania całego serwera
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {

    // Klasa ułatwia testy
    @Autowired
    private MockMvc mockMvc;

    // Repozytorium fałszywe - trzymające wszsytko w pamięci
    @Autowired
    private TaskRepository repo;

    // Odczyt konkretnego taska
    @Test
    void httpGet_returnsGivenTask() {
        // Dane
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        // Spodziewane
        // Bezpośrednio pod URL
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
