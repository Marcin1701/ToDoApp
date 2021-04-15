package pl.todoapp.MarcinRogozToDoApp.reports;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final TaskRepository taskRepository;

    private final PersistedTaskEventRepository eventRepository;

    ReportController(final TaskRepository taskRepository, final PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithCount(@PathVariable("id") Integer id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Klasa wewnętrzna
    private static class TaskWithChangesCount {

        public String description;

        public boolean done;

        public int changesCount;

        TaskWithChangesCount(final Task task, final List<PersistedTaskEvent> events){
            description = task.getDescription();
            done = task.isDone();
            changesCount = events.size();
        }

    }
}
