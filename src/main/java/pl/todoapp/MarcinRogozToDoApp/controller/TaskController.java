package pl.todoapp.MarcinRogozToDoApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import java.util.List;

// Coraz więcej funkcjonalności dajemy w kontrolerze a nie w Repozytorium
// W repo mamy naiwne nadpisywanie metod - czyli tylko adnotację dodajemy
// Dodajemy kolejną warstwę między kontrolerem a repozytorium - np dla User Story
// Czasami command handler uzywamy - kontroler dostaje komenty w metodach
// Czasami używa repo a czasami zewnętrzną usługę

// 1. (Interfejsy) (Clean architecture) Arhitektura portów i
// adapterów - opóźniamy implementację ile się da - dopiero na końcu implementujemy
// 2. Model jest sercem rozwiązania, encje które nie udostępniają getterów i setterów,
// modelujemy serce architektury, czyli mamy w modelu metody np. opłaćZamówienie, zarezerwujLot

// Wiążemy kontroler z istniejącym repozytorium
@RestController // Adnotacja Springowa - Repozytorium i Kontroler skanuje klasy przy uruchamianiu - zarządza nimi
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
    // W metodę wchodzimy tylko jeśli dostajemy jakieś parametry w URL
    // Jeżeli nie chcemy, żeby to tak działało to potrzebujemy dodać
    // Przed -> @GetMapping("/tasks")
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        // Loggerem dajemy znać że wsyzstkie taski ujawnione
        // Dostajemy warning przy próbie GET w postman o treści Exposing all the tasks!
        logger.warn("Exposing all the tasks!");
        // Wywyołamy metodę interfejsu findAll() i dostajemy wszystkie taski - metoda fabrykująca ok
        // Dostajemy kolekcję - Listę - Tasków
        // Nie dostajemy HATEOS już
        // Jak zrobić to? - Trzeba zależność Maven Spring HATEOS - dostajemy jeszcze 1 wrapper
        // Dokumentacja GOOGLE: spring io overriding spring data REST Reposnse Handlers
        return ResponseEntity.ok(repository.findAll());
    }

    // Jak chcemy stronicować w tym kontrolerze, trzeba nowy get mapping
    // Odpala się w pozostałych przypadkach
    // Już po samym mappingu spring wywoła metodę
    // Dzięki temu mamy sortowanie i stronicowanie przechodzące przez stworzony kontroler
    // Nie możemy tutaj dac List<Task>
    @GetMapping("/tasks")
    ResponseEntity<?> readAllTasks(Pageable page) {
        // Spring dodaje coś w stylu HATEOS np content
        logger.info("Custom pageable!");
        // Żeby użyć page - spring ma to wbudowane
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

}
