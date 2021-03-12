package pl.todoapp.MarcinRogozToDoApp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

// Nowa encja z grupami tasków
// Możemy podawać nazwę encji np @Entity(name = "nazwa")
// Wtedy wszędzie w query musimy dać "nazwa"
// Lepiej to zostawić i zrobić bez nadpisań
@Entity
@Table(name = "task_groups")
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Tasks groups description must not be empty")
    private String description;

    private boolean done;

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
    // Możliwe jest robienie relacji w jedną stronę - wewnątrz taska nie ma nic
    // @JoinColumn(referencedColumnName = "nazwa_kolumny")
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

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

    public void setDescription(String description) {
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

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }
}
