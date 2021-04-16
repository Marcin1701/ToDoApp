package pl.todoapp.MarcinRogozToDoApp.controller;

// Testy integracyjne

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

// Lekki test integracyjny
// Podpinamy nasz kontroler
@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {

    // CIęzko uzyskać task repository z kontekstu

    // Klasa ułatwia testy
    @Autowired
    private MockMvc mockMvc;

    // Repozytorium fałszywe - trzymające wszsytko w pamięci
    // Mockowanie beana - nie korzystamy z integration profilu
    @MockBean
    private TaskRepository repo;

    // Odczyt konkretnego taska
    @Test
    void httpGet_returnsGivenTask() {
        // Dane
        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task("foo", LocalDateTime.now())));

        // Spodziewane
        // Bezpośrednio pod URL
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks/123"))
                    .andDo(print())
                    .andExpect(content().string(containsString("\"description\":\"foo\"")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
