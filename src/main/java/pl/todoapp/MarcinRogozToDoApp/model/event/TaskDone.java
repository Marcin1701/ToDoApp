package pl.todoapp.MarcinRogozToDoApp.model.event;

import pl.todoapp.MarcinRogozToDoApp.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent{
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
