package pl.todoapp.MarcinRogozToDoApp.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.todoapp.MarcinRogozToDoApp.model.event.TaskDone;
import pl.todoapp.MarcinRogozToDoApp.model.event.TaskUndone;

// Nasłychujemy na wszystkie zdarzenia - zmiana stanu taska
// Procesowanie - usługa
@Service
public class ChangedTaskEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    // spring po typie domyśli się czego ma nasłuchiwać
    @EventListener
    public void on(TaskDone event){
        logger.info("Got " + event);
    }

    @EventListener
    public void on(TaskUndone event){
        logger.info("Got " + event);
    }


}
