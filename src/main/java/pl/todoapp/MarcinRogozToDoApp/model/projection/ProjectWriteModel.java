package pl.todoapp.MarcinRogozToDoApp.model.projection;

import pl.todoapp.MarcinRogozToDoApp.model.Project;
import pl.todoapp.MarcinRogozToDoApp.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;

// Model dedykowany pod ekrany - dużo rzeczy trzeba udostępniać na widoku
public class ProjectWriteModel {

    @NotBlank(message = "Project's description must not be empty!")
    private String description;

    @Valid
    private List<ProjectStep> steps;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(final List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        //  Dla każdego kroku dodajemy dostęp do projektu
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
