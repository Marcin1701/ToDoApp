package pl.todoapp.MarcinRogozToDoApp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

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

    // Jedna grupa ma w sobie wiele tasków
    // Lista nie jest najlepszym pomysłem, ale może być
    // Hibernate ma własne implementacje kolekcji
    // Hibernate nie ciągnie całej kolekcji dopóki nie jest ona potrzebna
    // Hibernate strzela SQL i pobiera listę wszystkich tasków (LAZY loading)
    // Lazy - dociągamy dane tylko wtedy kiedy są one potrzebne
    // Hibernate zaciągnie listę, ale nie jest powiedziane, że będzie ona po kolei
    // U nas akurat kolejność nie jest potrzebna (zawsze można posortować)
    // Zależy nam na unikalności - nieduplikowane taski
    // Można dodać cascade - jeśli zmieniamy grupę to zmieniamy wszytkie jej taski
    // private List<Task> tasks;
    // Implementacja
    // Wewnątrz taska grupa jest zmapowana jako pole group (W Task jest pole group)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;

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

    public Set<Task> getTasks() {
        return tasks;
    }

    void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }
}
