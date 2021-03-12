package pl.todoapp.MarcinRogozToDoApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.todoapp.MarcinRogozToDoApp.TaskConfigurationProperties;
import pl.todoapp.MarcinRogozToDoApp.model.*;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupReadModel;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        //var mockGroupRepository = mock(TaskGroupRepository.class);
        // Gdy ktoś na mockitowanym repo zawoła metodę existsIsDone... to możemy coś zrobić
        // Definiujemy z jakim argumentem ktoś metodę wywoła
        //when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // Jeśli ktoś wywoła
        //mockGroupRepository.existsByDoneIsFalseAndProject_Id(500); // to wpadamy do when i w zależności od tego co jest w parametrze zwracamy true lub false
        //assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500));

        // Kolejne potrzebne obiekty
        //var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        //when(mockTemplate.isAllowMultipleTasks()).thenReturn(false);
        // and
        //var mockConfig = mock(TaskConfigurationProperties.class);
        // Jesli ktoś zapyta w konfiguracji o template to zwracamy template, który utworzyliśmy wyżej
        //when(mockConfig.getTemplate()).thenReturn(mockTemplate);

        // metoda
        TaskConfigurationProperties mockConfig = configurationReturning(false);

        // obiekt do testowania
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);
        // wołanie metody - when
       // try {
        //    toTest.createGroup(LocalDateTime.now(), 0);
        //} catch(IllegalStateException e) {
            // sprawdzenie skutku - then
            // łapiemy wyjątek
          //  assertThat(e).isEqualTo()
        //}
        // Można też od razu połączyć then + when
        //assertThatThrownBy(() -> {
        //    toTest.createGroup(LocalDateTime.now(), 0);
       // }).isInstanceOf(IllegalStateException.class);
        // Albo
        //assertThatExceptionOfType(IllegalStateException.class)
        //        .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // Albo
        // Podział na 3 części w teście jest bardziej intuicyjny
        // Sprawdzamy treść wyjątku
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {

        //var mockTemplateRepository = mock(TaskConfigurationProperties.Template.class);
        //when(mockTemplateRepository.isAllowMultipleTasks()).thenReturn(true);

        //var mockConfig = mock(TaskConfigurationProperties.class);
        //when(mockConfig.getTemplate()).thenReturn(mockTemplateRepository);

        // Dane - given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // Do metody
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, null, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and no projects for given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        // Dane - given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // dane
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // dane
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, null, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOn_existingProject_createsAndSavesGroup() {
        // Dane
        var today = LocalDate.now().atStartOfDay();
        //dane
        var mockRepository = mock(ProjectRepository.class);

        var project = projectWith("bar", Set.of(-1, -2));
        // Zwracamy projekt z opisem bar zlożyony z -1 i -2
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        // dane
        InMemoryGroupRepository inMemoryGroupRepository = inMemoryGroupRepository();
        // dane
        int countBeforeCall = inMemoryGroupRepository.count();
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, inMemoryGroupRepository, mockConfig);
        // Daty są ważne
        // when
        GroupReadModel result = toTest.createGroup(today, 1);
        //then
        // sprawdzenie czy coś zapisało się na repozytorium
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1)
                .isEqualTo(inMemoryGroupRepository.count());

    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream().map(days -> {
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("foo");
            when(step.getDaysToDeadLine()).thenReturn(days);
            return step;
        }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    // Metoda unika redundancji
    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplateRepository = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplateRepository.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplateRepository);

        return mockConfig;
    }
    // Implementacja inMemory
    // Lepiej tak niż siłować się z mockito, bo będziemy utrzymywać w pamięci mape
    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements  TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(final TaskGroup entity) {
            if (entity.getId() == 0){
                try {
                    // Ustawienie trochę nielegalnie
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }
}
