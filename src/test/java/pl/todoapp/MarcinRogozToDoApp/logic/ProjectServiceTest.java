package pl.todoapp.MarcinRogozToDoApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    // Test jednostkowy
    // Do tego testu potrzebujemy obiektu project service
    // Do kontruktora przekazujemy własne parametry
    @Test
    // Jak wyświeltany jest w raportach
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_one_boMultipleGroupsConfig_And_openGroupExists_throwsIllegalStateException() {
        // Przygotowanie danych - given
        var mockGroupRepository = new TaskGroupRepository() {
            // Nadpisujemy metody, które są już zaimplementowane
            // Nie nadpisujemy SQLTaskGroupRepository, mamy interfejs
            // Własnie po to jest ten interfejs - nie musimy nadpisywać metod z JPARepository
            // Są 4 metody, ale nas interesuje tylko 1
            // Na szczęście przychodzi rozwiązanie, przetestowane, assertJ, JUNIT, mockito
            // Mockito pozwala w prostszy sposób interfejsy i implementować metody
            @Override
            public List<TaskGroup> findAll() {
                return null;
            }

            @Override
            public Optional<TaskGroup> findById(final Integer id) {
                return Optional.empty();
            }

            @Override
            public TaskGroup save(final TaskGroup entity) {
                return null;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(final Integer groupId) {
                return false;
            }
        }
        // wołanie metody - when
        // sprawdzenie skutku - then

    }
}
