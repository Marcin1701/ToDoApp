package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.todoapp.MarcinRogozToDoApp.logic.ProjectService;
import pl.todoapp.MarcinRogozToDoApp.model.Project;
import pl.todoapp.MarcinRogozToDoApp.model.ProjectStep;
import pl.todoapp.MarcinRogozToDoApp.model.projection.ProjectWriteModel;

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
    @PostMapping
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model) {
        // Dodanie projektu do bazy
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano projekt!");
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
    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.readAll();
    }
}
