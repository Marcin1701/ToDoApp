package pl.todoapp.MarcinRogozToDoApp.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// Klasa modelu Zadań
@Entity                 // Encja w bazie danych
@Table(name = "tasks")  // Dodawanie tabeli
public class Task {

    // Wymagany klucz w bazie danych
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // adnotacja która generuje automatycznie klucze
    private int id; // Nadawane wartości proste
    // Adnotacje walidujące javax.validation
    @NotBlank(message = "Tasks description must not be empty")
    // Nazwa inaczej w bazie, albo mapujemy pola albo na getterach
    private String description;

    private boolean done;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
