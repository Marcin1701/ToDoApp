package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    // Url do bazy danych
    // @Value("${spring.datasource.url}")
    // private String url;
    // Opcja z autowired - niezbyt dobra - jesteśmy zależni od springa
    @Autowired
    private DataSourceProperties dataSource;

    @Value("${my.prop}")
    private String myProp;

    @GetMapping("/info/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/info/prop")
    String myProp() {
        return myProp;
    }
}
