package pl.todoapp.MarcinRogozToDoApp.controller;

import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.todoapp.MarcinRogozToDoApp.logic.TaskService;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
//import pl.todoapp.MarcinRogozToDoApp.model.SqlTaskRepository;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
@RequestMapping("/tasks")   // Uproszczenie do metod - można usunąć /tasks
class TaskController {
    // Pole prywatne - Repozytorium - na nim działamy
    // Spring nie wstrzykuje TaskRepository - tylko warstwę pośrednią
    private final TaskRepository repository;

    // Dodajemy nowy obiekt TaskService
    private final TaskService taskService;

    // Zwracamy wszystkie taski i Logujemy informację - "Uwaga zapytaliśmy bazę danych i pobraliśmy Taski"
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    // Konstruktor - wymaga TaskRepository - Bean springowy
    // Spring najpierw tworzy TaskRepository, a dopiero potem wstrzukuje go do TaskController
    // Adotacja @Autowired była kiedyś potrzebna - teraz już nie
    // Jak to wykorzystać - np nadpisać metodę zwracającą Taski

    // Żeby naprawić błąd dwóch sqlTaskRepository - możemy dawać - wiążemy się ze springiem @Qualifier, można lepiej
    // Qualifier zepsułby testy
    // TaskController(@Qualifier("sqlTaskRepository") final TaskRepository repository)
    // Adnotacja @Lazy - bean ma być leniwie dodawany
    // Np repozytorium ma implementację i 1 zależy od 2 a 2 od 1, brzydkie obejście to dodanie @Lazy w 1 miejscu
    TaskController(final TaskRepository repository, final TaskService taskService) {
        this.repository = repository;
        this.taskService = taskService;
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
    @GetMapping(params = {"!sort", "!page", "!size"})
    //ResponseEntity<List<Task>> readAllTasks() {
    // Springowe kontrolery mogą zwracać completable future
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        // Loggerem dajemy znać że wsyzstkie taski ujawnione
        // Dostajemy warning przy próbie GET w postman o treści Exposing all the tasks!
        logger.warn("Exposing all the tasks!");
        // Wywyołamy metodę interfejsu findAll() i dostajemy wszystkie taski - metoda fabrykująca ok
        // Dostajemy kolekcję - Listę - Tasków
        // Nie dostajemy HATEOS już
        // Jak zrobić to? - Trzeba zależność Maven Spring HATEOS - dostajemy jeszcze 1 wrapper
        // Dokumentacja GOOGLE: spring io overriding spring data REST Reposnse Handlers

        // Korzystamy z asynchroniczności
        return taskService.findAllAsync().thenApply(ResponseEntity::ok);

        //return ResponseEntity.ok(repository.findAll());
    }

    // Jak chcemy stronicować w tym kontrolerze, trzeba nowy get mapping
    // Odpala się w pozostałych przypadkach
    // Już po samym mappingu spring wywoła metodę
    // Dzięki temu mamy sortowanie i stronicowanie przechodzące przez stworzony kontroler
    // Nie możemy tutaj dac List<Task>
    @GetMapping
    ResponseEntity<?> readAllTasks(Pageable page) {
        // Spring dodaje coś w stylu HATEOS np content
        logger.info("Custom pageable!");
        // Żeby użyć page - spring ma to wbudowane
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        logger.info("Get Method!");
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Principal p - dostęp do użytkownika zalogowanego
    // Servletowe metody
    // Stara metoda
    // Czasami warto jej używać ale niekoniecznie
    @GetMapping("/test")
    void old(HttpServletRequest req, HttpServletResponse resp) {
        req.getParameter("foo");    // Pobieraj parametr

    }


    // Jeśli ktoś wyśle żądanie accept aplication json
    // Chodzi o to żeby nie wymuszać trafienia w metodę
     @GetMapping(value = "/search/done") // nie wymagamy parametrów ale jak ktoś je poda to obsługujemy
     ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) { // Generalnie parametr jest wymagany, ale możemy określić domyślny
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    // Jeśli ktoś wyśle żądanie accept aplication xml
    // @GetMapping(value = "/search/done", produces = MediaType.TEXT_XML_VALUE)
    // String bar() { return "";}




    // Zwracamy nowy task
    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        logger.info("Post Method!");
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    // Metoda PUT
    // Zaznaczamy w springu {parametr} że będzie jakiś id w którym akualizujemy zadanie
    // Put da nam jako parametr nowego, zaktualizowanego taska
    // Ciało musi być zwalidowane @Valid
    // @Path Variable pozwala wziąć zmienną z id
    // Jeśli nazwa zmiennej nie jest taka sama jak w adresie to dajemy @PathVariable("nazwa_w_URL) nazwa_zmiennej
    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Alternatywa dla Patch metody z transactional
         repository.findById(id)
                .ifPresent(task -> {
                            task.updateFrom(toUpdate);
                            repository.save(task);
                        });
        // Zapisz implementację
        // repository.save(toUpdate);
        return ResponseEntity.noContent().build();
    }

    // Programowanie aspektowe
    // Metoda patch - po jej wykonaniu odwracamy stan taska z done na !done
    // Każda metoda z transactional ma begin i commit - metoda musi być publiczna
    // Musimy mieć beana aspektowego

    // Transactional jest elementem programowania aspektowego
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        // Jeśli nie zostanie zwrócony obiekt/zmienna to transakcja nie zostanie wykonana
        // i zapisana w DB, np po rzuceniu wyjątku w tym miejscu (albo innym)
        // throw new RuntimeException();
        return ResponseEntity.noContent().build();
    }

    // Transactional działa bo nie wywołuje bezpośrednio metody
    // Bezpośrednie wywołanie nie zadziała
    // Trzeba by metodę foobar wołać z zewnątrz
    //public void foobar() {
     //   this.toggleTask(1);
    //}
}
