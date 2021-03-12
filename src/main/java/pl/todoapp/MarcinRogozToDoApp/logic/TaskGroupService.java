package pl.todoapp.MarcinRogozToDoApp.logic;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;
import pl.todoapp.MarcinRogozToDoApp.model.TaskGroupRepository;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupReadModel;
import pl.todoapp.MarcinRogozToDoApp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

// Serwis jest warstwą pośrednią między repozytorium a kontrolerem

// Są serwisy aplikacyjne i domenowe
// Aplikacyjny - bliskie user story
// Istnieją zakresy jak obiekt powinien być wstrzykiwany (singleton, 1 obiekt TaskGroupService na całą aplikację)
@Service    // Adnotacja owija component
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)    // Klasa jako szablon
// Ratuje nas w wielu sytuacjach
@RequestScope // W obrębie 1 żądania mamy 1 instancję klasy
public class TaskGroupService {

   private final TaskGroupRepository repository;

   private final TaskRepository taskRepository;

   // Jeśli TaskGroup service jest w 2 miejsach
   // Liczenie np ilości wywołań
    // private int count; (jeśli uderzy apkę wieloma wątkami to może się sypnąć, aby tego uniknąć używamy scope)

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(final GroupWriteModel source){
        // Jeśli nie wiemy co jest zwracane z funkcji po prawej stronie - lepiej zamiast var dawać od razu typ
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    // Daną grupę chcemy zakończyć - toggle
    public void toggleGroup(int groupId) {
        // Pobieramy grupę i mamy konfigurację - taskconfigproperties
        // Nie możemy zaznaczyć grupy jako done - jeśli wszystkie taski nie są done
        // Skąd wiemy czy są done - mamy taskRepository
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first!");
        }
        // Jeżeli grupa jest to ją dostaniemy, a jeżeli nie to wyjątek
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found!"));
        result.setDone(!result.isDone());
        // Zapis na samym końcu
        repository.save(result);
    }

}
