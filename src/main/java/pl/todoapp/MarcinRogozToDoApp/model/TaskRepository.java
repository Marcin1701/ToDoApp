package pl.todoapp.MarcinRogozToDoApp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pl.todoapp.MarcinRogozToDoApp.model.Task;

import java.util.List;
import java.util.Optional;

// Tylko metody które nas interesują
public interface TaskRepository {

    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer i);

    boolean existsById(Integer id);

    Task save(Task entity);

    List<Task> findByDone(boolean done);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    // Użycie Query wygenerowanego przez springa
    List<Task> findAllByGroup_Id(Integer groupId);
}
