package pl.todoapp.MarcinRogozToDoApp.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.todoapp.MarcinRogozToDoApp.logic.ProjectService;
import pl.todoapp.MarcinRogozToDoApp.model.Project;
import pl.todoapp.MarcinRogozToDoApp.model.ProjectStep;
import pl.todoapp.MarcinRogozToDoApp.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")    // Nazwa jak w pliku html
// Po wejściu na strone pod adres projects - kontroler się uruchamia
public class ProjectController {

    private final ProjectService service;

    public ProjectController(final ProjectService service) {
        this.service = service;
    }


    // Zwróć żądanie http zmapowaną na metodę
    // Jeśli spring dostaje zwrotkę String
    // To renderuje szablon z templates
    @GetMapping         // interfejs model pozwala na komunikację z kontrolerem
    String showProjects(Model model) {
        // Ten model chcemy zachowac
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    // Metoda dodaje projekt - domyślny post submit
    // Dodajemy walidację - skupiamy się na kontrolerze projektu
    // Technika do walidacji Binding Result
    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current,
                      BindingResult bindingResult,
                      Model model)
    {
        // Jeśli binding zawiera błędy wróć do widoku głównego
        // Thymeleaf zwróci nam błąd
        if (bindingResult.hasErrors()){
            return "projects";
        }
        // Dodanie projektu do bazy
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());

        // Pomijamy cachowanie - nowy projekt będzie widoczny od razu po dodaniu
        model.addAttribute("projects", getProjects());

        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    // Micrometer służy do mierzenia różnych rzeczy
    // Mierzymy czas - przechowujemy histogram - wartość określa dla których właściwości chcemy mieć jakieś miary
    // Możemy mierzyć jak aplikacja działa dla użytkowników
    // Metody z taką adnotacją wysyłają metryki
    // Jeśli wołamy przez springa metodę to aspekty są dodane - jeżeli przekierujemy do metody createGroup to nie zadziała
    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,    // aktualny model
            Model model,    // komunikaty dodatkowe
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline  // specyficzny format zapisu - Spring oferuje adnotację
    )
    {
        try {
            service.createGroup(deadline, id);
            model.addAttribute("message", "Dodano grupę!");
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }
    // Post reaguje wtedy kiedy pojawia się parametr addStep
    @PostMapping(params = "addStep")    // Parametr pochodzący z modelu project
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        // Pojawią się nowe kroki
        current.getSteps().add(new ProjectStep());
        return "projects";
    }


    // Metoda zawsze dostarcza projekty
    // Bez tego zawsze trzeba zwracać projekt - tak jest lepiej
    // Dla wystąpienia atrybutu projects w HTML zwróć listę

    // UWAGA problem - jesli dodajemy nowy projekt i wyślemy formularz - "Dodaj"
    // Nowy projekt nie będzie widoczny na głównej stronie zaraz po dodaniu
    // Spring cachuje - stara się nie pytać zbyt często BD
    // Jeśli nie chcemy tego - od razu widoczne
    // Dodajemy
    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.readAll();
    }
}
