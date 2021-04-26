package pl.todoapp.MarcinRogozToDoApp.model.projection;

import pl.todoapp.MarcinRogozToDoApp.model.Project;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {

    // Dodajemy walidację
    @NotBlank(message = "Tasks groups description must not be empty")
    private String description;

    @Valid
    private List<GroupTaskWriteModel> tasks = new ArrayList<>();

    public GroupWriteModel() {
        // Pusty task do renderowania pola formularza
        tasks.add(new GroupTaskWriteModel());
    }

    public GroupWriteModel(TaskGroup taskGroup) {
        this.description = taskGroup.getDescription();
        this.tasks = taskGroup.getTasks()
                .stream()
                .map(GroupTaskWriteModel::new)
                .collect(Collectors.toList());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(final List<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(final Project project) {
        var result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );
        result.setProject(project);
        return result;
    }


}
