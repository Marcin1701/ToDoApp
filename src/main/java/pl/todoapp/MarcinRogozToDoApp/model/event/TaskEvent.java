package pl.todoapp.MarcinRogozToDoApp.model.event;

// Zdarzenia dotyczące tasków

import pl.todoapp.MarcinRogozToDoApp.model.Task;

import java.time.Clock;
import java.time.Instant;

// Klasa abstrakcyjna - baza
public abstract class TaskEvent {

    // Pierwszy sposób tworzenia zdarzenia
    // Metoda fabryczna
    public static TaskEvent changed(Task source) {
        // Jeśli task zrobiony to zwracamy isDone
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    // Identyfikator taska
    private int taskId;

    // Czas wystąpienia taksa
    // Punkt w czasie
    private Instant occurrence;

    // Tylko task ID
    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        // Zegarek w czasie
        this.occurrence = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
