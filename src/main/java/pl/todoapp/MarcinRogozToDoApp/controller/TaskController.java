package pl.todoapp.MarcinRogozToDoApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

// Wiążemy kontroler z istniejącym repozytorium
@RepositoryRestController   // Adnotacja Springowa - Repozytorium i Kontroler skanuje klasy przy uruchamianiu - zarządza nimi
class TaskController {
    // Pole prywatne - Repozytorium - na nim działamy
    private final TaskRepository repository;

    // Zwracamy wszystkie taski i Logujemy informację - "Uwaga zapytaliśmy bazę danych i pobraliśmy Taski"
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);


    // Konstruktor - wymaga TaskRepository - Bean springowy
    // Spring najpierw tworzy TaskRepository, a dopiero potem wstrzukuje go do TaskController
    // Adotacja @Autowired była kiedyś potrzebna - teraz już nie
    // Jak to wykorzystać - np nadpisać metodę zwracającą Taski
    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }
}
