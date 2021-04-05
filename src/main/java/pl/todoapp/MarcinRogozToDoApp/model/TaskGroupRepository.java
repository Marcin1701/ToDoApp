package pl.todoapp.MarcinRogozToDoApp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

    boolean existsByDoneIsFalseAndProject_Id(Integer groupId);

    List<Task> findAllTaskInGroup(int id);

    boolean existsByDescription(String description);
}
