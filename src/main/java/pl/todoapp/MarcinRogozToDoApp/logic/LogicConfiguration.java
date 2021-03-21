package pl.todoapp.MarcinRogozToDoApp.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.todoapp.MarcinRogozToDoApp.TaskConfigurationProperties;
import pl.todoapp.MarcinRogozToDoApp.model.ProjectRepository;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;


// Zamiast adnotować klasy @Service
// Możemy stworzyć klasę konfiguracyjną  - nie jesteśmy zależni od springa
// Konfiguracjae nie musi być publiczna - może być pakietowa
@Configuration
// Konfiguracja XML - jak to kiedyś wyglądało
//@ImportResource("classpath:applicationContext.txt")
class LogicConfiguration {

    // Spring wstrzyknie do beana obiekty
    // Spring wie kiedy stworzyć
    @Bean
    ProjectService projectService(final ProjectRepository repository,
                           final TaskGroupRepository taskGroupRepository,
                           final TaskConfigurationProperties config,
                                  final TaskGroupService taskGroupService)
    {
        return new ProjectService(repository, taskGroupRepository, config, taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository repository,
                                      final TaskRepository taskRepository)
    {
        return new TaskGroupService(repository, taskRepository);
    }

}
