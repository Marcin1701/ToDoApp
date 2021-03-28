package pl.todoapp.MarcinRogozToDoApp.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline() {
        // Dane
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("Bar", null)));

        // Kiedy
        var result = new GroupReadModel(source);
        // Robimy compare to na taskach które nie mają usatwionego deadline = null
        // Test przed poprawką walił NullPointerException

        // Potem
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}
