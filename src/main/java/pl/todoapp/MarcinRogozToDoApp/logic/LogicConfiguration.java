package pl.todoapp.MarcinRogozToDoApp.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.todoapp.MarcinRogozToDoApp.TaskConfigurationProperties;
import pl.todoapp.MarcinRogozToDoApp.model.ProjectRepository;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;


// Zamiast adnotować klasy @Service
// Możemy stworzyć klasę konfiguracyjną  - nie jesteśmy zależni od springa
// Konfiguracjae nie musi być publiczna - może być pakietowa
@Configuration
class LogicConfiguration {

    // Spring wstrzyknie do beana obiekty
    // Spring wie kiedy stworzyć
    @Bean
    ProjectService service(final ProjectRepository repository,
                           final TaskGroupRepository taskGroupRepository,
                           final TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, config);
    }
}
