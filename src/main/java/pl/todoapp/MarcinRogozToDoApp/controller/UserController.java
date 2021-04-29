package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.todoapp.MarcinRogozToDoApp.logic.UserService;
import pl.todoapp.MarcinRogozToDoApp.model.User;
import pl.todoapp.MarcinRogozToDoApp.model.projection.UserWriteModel;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
     ModelAndView registerPage(Model model) {
        model.addAttribute("user", new UserWriteModel());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping
    public ModelAndView registerUser(@Valid UserWriteModel userWriteModel, BindingResult bindingResult, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
            return modelAndView;
        }
        userService.saveUserInDatabase(userWriteModel);
        model.addAttribute("user", userWriteModel);
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
