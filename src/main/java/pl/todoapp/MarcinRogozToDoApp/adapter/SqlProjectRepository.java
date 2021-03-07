package pl.todoapp.MarcinRogozToDoApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.todoapp.MarcinRogozToDoApp.model.Project;
import pl.todoapp.MarcinRogozToDoApp.model.ProjectRepository;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;

import java.util.List;

// Adapter - styl zaimplementowania repozytorium
// Można to podmienić np na inne repozytorium
@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    // Zabezpieczenie przed n + 1 selectów
    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();
}
