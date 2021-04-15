package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.todoapp.MarcinRogozToDoApp.TaskConfigurationProperties;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/info")
public class InfoController {

    // Url do bazy danych
    // @Value("${spring.datasource.url}")
    // private String url;
    // Opcja z autowired - niezbyt dobra - jesteśmy zależni od springa
   // @Autowired
    //private DataSourceProperties dataSource
    //@Value("${task.allowMultipleTasksFromTemplate}")
    //private TaskConfigurationProperties myProp;

    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;

    // Jeśli mamy kilka konstruktorów trzeba dodawać autowired

    InfoController(final DataSourceProperties dataSource, final TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    // Podajemy rolę z jaką możemy wejści do metody
    @Secured("ROLE_ADMIN")
    @GetMapping("/url")
    String url() {
        return dataSource.getUrl();
    }

    // Adnotacja z javax
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
        //return myProp.isAllowMultipleTasksFromTemplate();
    }
}
