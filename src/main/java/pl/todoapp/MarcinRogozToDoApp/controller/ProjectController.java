package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }
}
