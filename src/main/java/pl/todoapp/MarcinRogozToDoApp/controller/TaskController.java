package pl.todoapp.MarcinRogozToDoApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    // Metoda zwraca listę wszystkich tasków
    // ResponseEntity - abstrakcja reprezentuje odpowiedź
    // Możemy ustawić jaki ma być status code np 204 - ? oznacza że nie typujemy odpowiedzi
    // Można też:
    // List<Task> findAllTasks() {} - spring będzie wiedział że zwraca JSON - Task ma być publiczne
    // Taka abstrakcja jak poniżej jest lepsza
    // Trzeba nadpisać metodę - Mówimy: ta metoda jest używana zamiast tej z repozytorium
    // @RequestMapping - jak tylko przychodzi request to ma trafić do tej metody
    // Trzeba doprecyzować jaki request przychodzi
    // method = -> domyślnie ustawiona jest na GET
    // @RequestMapping(method = RequestMethod.GET, path = "/tasks")
    // Poszczególne requesty mają swoje dedykowane mappingi
    // Można skrótowo -> GetMapping ustawia pod spodem RequestMapping automatycznie
    // Jak nie ma nic w nawiasie -> Tutaj jest od razu value = "/tasks", value jest powiązany aliasem z RequestMapping
    @GetMapping("/tasks")
    ResponseEntity<?> readAllTasks() {
        // Loggerem dajemy znać że wsyzstkie taski ujawnione
        logger.warn("Exposing all the tasks!");
        // Wywyołamy metodę interfejsu findAll() i dostajemy wszystkie taski - metoda fabrykująca ok
        // Dostajemy kolekcję - Listę - Tasków
        return ResponseEntity.ok(repository.findAll());
    }
}
