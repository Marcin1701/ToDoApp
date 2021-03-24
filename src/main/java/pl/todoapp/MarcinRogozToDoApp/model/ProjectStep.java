package pl.todoapp.MarcinRogozToDoApp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "project_steps")
public class ProjectStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Project step's description must not be empty")
    private String description;

    private int daysToDeadline;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getDaysToDeadLine() {
        return daysToDeadline;
    }

    public void setDaysToDeadLine(final int daysToDeadLine) {
        this.daysToDeadline = daysToDeadLine;
    }

    Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }
}
