package pl.todoapp.MarcinRogozToDoApp.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// Klasa modelu Zadań
@Entity                 // Encja w bazie danych
@Table(name = "tasks")  // Dodawanie tabeli
public class Task {

    // Wymagany klucz w bazie danych
    @Id
    private int id;
    // Nazwa inaczej w bazie, albo mapujemy pola albo na getterach
    @Column(name = "desc")
    private String description;
    private boolean done;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
