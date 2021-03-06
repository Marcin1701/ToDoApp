package pl.todoapp.MarcinRogozToDoApp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// Nowa encja z grupami tasków
@Entity
@Table(name = "tasks_groups")
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Tasks groups description must not be empty")
    private String description;

    private boolean done;

    @Embedded
    private Audit audit = new Audit();

    public TaskGroup() {
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
}
