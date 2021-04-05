package pl.todoapp.MarcinRogozToDoApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;

import java.util.Set;


// Klasa bazowa dla zdarzeń Spring
// Obsługa zdarzeń
@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {

    public static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    // Bazowa grupa
    private final TaskGroupRepository taskGroupRepository;

    Warmup(final TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    // Można zamodelować cachowanie
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed!");
        final String description = "ApplicationContextEvent";
        if (!taskGroupRepository.existsByDescription(description)) {
            logger.info("Not required group found! Adding it!");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosedEvent", null, group),
                    new Task("ContextRefreshedEvent", null, group),
                    new Task("ContextStoppedEvent", null, group),
                    new Task("ContextStartedEvent", null, group)
            ));
            taskGroupRepository.save(group);
        }
    }
}
