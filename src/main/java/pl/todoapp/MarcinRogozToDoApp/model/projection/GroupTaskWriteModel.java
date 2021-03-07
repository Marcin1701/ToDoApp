package pl.todoapp.MarcinRogozToDoApp.model.projection;

import pl.todoapp.MarcinRogozToDoApp.model.Task;

import java.time.LocalDateTime;

// Są to podzbiory umożliwiające utworzenie pełnoprawnych tasków - używane w serwisach
class GroupTaskWriteModel {

    // JEST TO DTO - data transfer object - używany do przesyłania danych w bezpieczny sposób

    private String description;

    private LocalDateTime deadline;

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

    public Task toTask() {
        return new Task(this.description, this.deadline);
    }
}
