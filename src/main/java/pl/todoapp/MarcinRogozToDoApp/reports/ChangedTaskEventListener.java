package pl.todoapp.MarcinRogozToDoApp.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.todoapp.MarcinRogozToDoApp.model.event.TaskDone;
import pl.todoapp.MarcinRogozToDoApp.model.event.TaskEvent;
import pl.todoapp.MarcinRogozToDoApp.model.event.TaskUndone;

// Nasłychujemy na wszystkie zdarzenia - zmiana stanu taska
// Procesowanie - usługa
@Service
public class ChangedTaskEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    ChangedTaskEventListener(final PersistedTaskEventRepository repository) {
        this.repository = repository;
    }
    // spring po typie domyśli się czego ma nasłuchiwać
    @EventListener
    public void on(TaskDone event){
       onChanged(event);
    }

    @EventListener
    public void on(TaskUndone event){
        onChanged(event);
    }

    private void onChanged(final TaskEvent event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }

}
