package pl.todoapp.MarcinRogozToDoApp.logic;

import pl.todoapp.MarcinRogozToDoApp.TaskConfigurationProperties;
import pl.todoapp.MarcinRogozToDoApp.model.*;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupReadModel;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupTaskWriteModel;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupWriteModel;
import pl.todoapp.MarcinRogozToDoApp.model.projection.ProjectWriteModel;

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
    // Spring przy stosowaniu Proxy używa cglib
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties taskConfigurationProperties;
    private final TaskGroupService service;

    // Trzeba dodawać autowired, jeśli mamy kilka konstruktorów

    ProjectService(final ProjectRepository projectRepository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties taskConfigurationProperties, final TaskGroupService taskGroupService) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
        this.service = taskGroupService;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        // Trzeba przetestować 2 przypadki
        if (!taskConfigurationProperties.getTemplate().isAllowMultipleTasks() &&
                taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed!");
        }
        // Coś w repo zostało znalezione lub nie
        GroupReadModel targetGroup = projectRepository.findById(projectId)
                .map(project -> {
                    var result = new GroupWriteModel();
                    result.setDescription(project.getDescription());
                    result.setTasks(
                            project.getSteps().stream()
                                    .map(step -> {
                                            var task = new GroupTaskWriteModel();
                                            task.setDescription(step.getDescription());
                                            task.setDeadline(deadline.plusDays(step.getDaysToDeadLine()));
                                            return task;
                                            }
                                    ).collect(Collectors.toList())
                    );
                    return service.createGroup(result, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return targetGroup;
    }
}
