package pl.todoapp.MarcinRogozToDoApp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

// Encje są zarządzane przez enitity managera
// Metody persist itd

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

    // UWAGA WAŻNE!!!!
    // W DB kolumny często są nazywane z podkreśleniami: surname_and_name
    // Tabele są w liczbie mnogiej
    // Spring mapuje takie nazwy do camel case: surnameAndName

    // Format daty do postmana "2020-12-23T23:59:59.999"
    private LocalDateTime deadline;

    // Tych pól nie udostępniamy w JSON
    // Kolumna kiedy task stworzyliśmy\
    // Ciekawostka: jest adnotacja polska @PESEL
    // Jeśli nie chcemy mapować zmiennej na kolumnę w bazie dajemy: @Transient
    private LocalDateTime createdOn;
    // Kiedy zaktualizowana
    private LocalDateTime updatedOn;

    public Task() {
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    // Kiedy zczytamy encję
    public void updateFrom(final Task source) {
        this.description = source.description;
        this.done = source.done;
        this.deadline = source.deadline;
    }

    // Funkcja odpali się przed zapisem do DB - zazwyczaj stosowana do insertów
    @PrePersist
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    // Wykonywana przed dokładaniem encji do managera
    @PreUpdate
    void preMerge() {
        updatedOn = LocalDateTime.now();
    }
}
