package pl.todoapp.MarcinRogozToDoApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.todoapp.MarcinRogozToDoApp.logic.TaskGroupService;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupReadModel;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupWriteModel;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {

    private final TaskRepository repository;

    private final TaskGroupService service;

    private final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);


    TaskGroupController(final TaskRepository repository, final TaskGroupService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        logger.info("Creating group!");
        GroupReadModel result = service.createGroup(toCreate);
        // getId() - upublicznić
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("Exposing all task groups!");
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}/tasks")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        logger.info("Exposing tasks in group");
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    ResponseEntity<TaskGroup> toggleGroup(@PathVariable int id) {
        logger.info("Changed group status!");
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }
    // TODO Zrobić mapowanie tasków niezrobionych oraz tasków na dzisiaj Zadanie na 6 Spring Web

    // Obsługa wyjątków
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
