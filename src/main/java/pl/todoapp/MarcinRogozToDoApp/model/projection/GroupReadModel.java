package pl.todoapp.MarcinRogozToDoApp.model.projection;

import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {

    private int id;

    private String description;

    /**
    * Deadline z ostatniego taska w grupie
    */
    private LocalDateTime deadline;

    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadline)
                // Jeśli nie ma deadline (null) i robimy compare to dostaniemy nullpointerexception
                // Odfiltrujemy sobie nulle
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks()
                .stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public int getId() { return id; }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public  void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }

}
