package pl.todoapp.MarcinRogozToDoApp.adapter;

import org.hibernate.annotations.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;

import javax.persistence.NamedNativeQuery;
import java.util.List;

// Adapter - styl zaimplementowania repozytorium
// Można to podmienić np na inne repozytorium
@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    // Zabezpieczenie przed n + 1 selectów
    @Override
    // Domyślnie jest inner join
    @Query("select distinct g from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();

    // Wszystkie niezrobione grupy w projekcie
    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer groupId);

    @Query("select t from Task t, TaskGroup g join g.tasks")
    List<Task> findAllTaskInGroup(int id);

}
