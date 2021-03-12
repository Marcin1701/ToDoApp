package pl.todoapp.MarcinRogozToDoApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.todoapp.MarcinRogozToDoApp.TaskConfigurationProperties;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    // Test jednostkowy
    // Do tego testu potrzebujemy obiektu project service
    // Do kontruktora przekazujemy własne parametry
    @Test
    // Jak wyświeltany jest w raportach
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_one_boMultipleGroupsConfig_And_openGroupExists_throwsIllegalStateException() {
        // Przygotowanie danych - given
        //var mockGroupRepository = new TaskGroupRepository() {
            // Nadpisujemy metody, które są już zaimplementowane
            // Nie nadpisujemy SQLTaskGroupRepository, mamy interfejs
            // Własnie po to jest ten interfejs - nie musimy nadpisywać metod z JPARepository
            // Są 4 metody, ale nas interesuje tylko 1
            // Na szczęście przychodzi rozwiązanie, przetestowane, assertJ, JUNIT, mockito
            // Mockito pozwala w prostszy sposób interfejsy i implementować metody
            //@Override
            //public List<TaskGroup> findAll() {
            //    return null;
           // }

           // @Override
           // public Optional<TaskGroup> findById(final Integer id) {
            //    return Optional.empty();
            //}

            //@Override
            //public TaskGroup save(final TaskGroup entity) {
            //    return null;
            //}

           // @Override
            //public boolean existsByDoneIsFalseAndProject_Id(final Integer groupId) {
            //    return false;
            //}
       // }

        // WYKORZYSTANIE MOCKITO
        // Mechanizm refleksji pozwala nadpisywać metody
        var mockGroupRepository = mock(TaskGroupRepository.class);
        // Gdy ktoś na mockitowanym repo zawoła metodę existsIsDone... to możemy coś zrobić
        // Definiujemy z jakim argumentem ktoś metodę wywoła
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        // Jeśli ktoś wywoła
        //mockGroupRepository.existsByDoneIsFalseAndProject_Id(500); // to wpadamy do when i w zależności od tego co jest w parametrze zwracamy true lub false
        //assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500));

        // Kolejne potrzebne obiekty
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(false);
        // and
        var mockConfig = mock(TaskConfigurationProperties.class);
        // Jesli ktoś zapyta w konfiguracji o template to zwracamy template, który utworzyliśmy wyżej
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        // obiekt do testowania
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);
        // wołanie metody - when
        toTest.createGroup(LocalDateTime.now(), 0);
        // sprawdzenie skutku - then

    }
}
