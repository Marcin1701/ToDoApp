package pl.todoapp.MarcinRogozToDoApp.reports;

import pl.todoapp.MarcinRogozToDoApp.model.event.TaskEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

// Niby klasa a jednak struktura
// Przechowamy ją w bazie danych
// Będzie informowała o zdarzeniach

@Entity
@Table("task_event")
class PersistedTaskEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private LocalDateTime occurrence;

    private int taskId;

    // JPA więc konstruktor bezparametrowy
    public PersistedTaskEvent() {
    }

    PersistedTaskEvent(TaskEvent source) {
        taskId = source.getTaskId();
        name = source.getClass().getSimpleName();
        // Przerabiamy z Instant na LocalDateTime
        occurrence = LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
    }
}
