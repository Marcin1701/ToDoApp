package pl.todoapp.MarcinRogozToDoApp.logic;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.todoapp.MarcinRogozToDoApp.TaskConfigurationProperties;
import pl.todoapp.MarcinRogozToDoApp.model.*;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Testy głównie w warstwie serwisów

/**
 * Klasa się nie skompiluje jeśli nie mamy dostępu do Spring
 * 2 szkoły - albo poza springiem tworzymy komponenty, albo w
 */

// Bez service jest to czysta klasa javy - korzysta z samych klas javy
// Konfiguracja beanem nam wstrzykuje zależności do spring - klasa nie jest zależna
    // To ma sens kiedy wiemy, że będziemy chcieli szybko się pozbyć springa
    // U nas kontroler zależy od springa i tak więc tutaj to niepotrzebne - tylko tak że można to zrobić
//@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties taskConfigurationProperties;

    // Trzeba dodawać autowired, jeśli mamy kilka konstruktorów

    ProjectService(final ProjectRepository projectRepository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties taskConfigurationProperties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        // Trzeba przetestować 2 przypadki
        if (!taskConfigurationProperties.getTemplate().isAllowMultipleTasks() &&
                taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed!");
        }
        // Coś w repo zostało znalezione lub nie
        TaskGroup targetGroup = projectRepository.findById(projectId)
                .map(project -> {
                    var result = new TaskGroup();
                    result.setDescription(project.getDescription());
                    result.setTasks(
                            project.getSteps().stream()
                                    .map(step -> new Task(
                                            step.getDescription(),
                                            deadline.plusDays(step.getDaysToDeadLine()))
                                    ).collect(Collectors.toSet())
                    );
                    // kolejna poprawka
                    result.setProject(project);
                    // Poprawka po teście
                    return taskGroupRepository.save(result);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(targetGroup);
    }
}
