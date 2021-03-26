package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.todoapp.MarcinRogozToDoApp.model.ProjectStep;
import pl.todoapp.MarcinRogozToDoApp.model.projection.ProjectWriteModel;

@Controller
@RequestMapping("/projects")    // Nazwa jak w pliku html
// Po wejściu na strone pod adres projects - kontroler się uruchamia
public class ProjectController {

    // Zwróć żądanie http zmapowaną na metodę
    // Jeśli spring dostaje zwrotkę String
    // To renderuje szablon z templates
    @GetMapping         // interfejs model pozwala na komunikację z kontrolerem
    String showProjects(Model model) {
        // Ten model chcemy zachowac
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    // Post reaguje wtedy kiedy pojawia się parametr addStep
    @PostMapping(params = "addStep")    // Parametr pochodzący z modelu project
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        // Pojawią się nowe kroki
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

}
