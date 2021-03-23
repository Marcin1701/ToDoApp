package pl.todoapp.MarcinRogozToDoApp.logic;

// Podczas wysyłania zapytań do bazy danych np do pobrania wszystkich tasków
// wykonanie zapytania może długo trwać, spróbujemy zastosować wielowątkowość
// Do tego potrzebny jest serwis TaskService

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    public static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository repository;

    TaskService(final TaskRepository repository) {
        this.repository = repository;
    }

    @Async
    // Mechanizm z JAVA8+ Compitable Future, jak promise w C++
    public CompletableFuture<List<Task>> findAllAsync() {
        // W sposób asynchroniczny - na osobnym wątku odpytujemy repozytorium
        // W springu jest adnotacja @Async
        logger.info("Supply async!");
        return CompletableFuture.supplyAsync(repository::findAll);
    }



}
