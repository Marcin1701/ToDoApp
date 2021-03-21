package pl.todoapp.MarcinRogozToDoApp;

import com.zaxxer.hikari.util.DriverDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import javax.sql.DataSource;
import java.util.*;

// Konfiguracja testów
// Mamy własną wersję repozytorium
// Test są teraz niezależne od bazy danych - repo symuluje bazę
// Aplikacja się nie uruchamia - mamy błąd - zbyt dużo beanów o tej samej nazwie
// Aplikacja się uruchamia, dwie implementacje repozytorium to też błąd
@Configuration
public class TestConfiguration {

    // Kiedy nie mamy profilu integration
    // Bean z bazą
    // Każdy inny profil używa tego
    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eDataSource() {
        // mem: to baza w pamięci

        // Błąd  Schema-validation: missing table [project_steps] dodać DB_CLOSE_DELAY
        // Baza się nie zamknie kiedy ostatnie połacznenie się nie zamknie
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    //@Primary    // Adnotacja oznacza, że to repo jest priorytetowe, ale znowu to psuje, bo inny test łączący się
    // z DB używać będzie primary
    //@ConditionalOnMissingBean // Słuchaj - to task repository obowiązuje tylko jeśli nie ma innego repo (mamy inne repo, nie działa)
    //@ConditionalOnProperty  // Dla zdefiniowanej właściwości to task repo obowiązuje
    // Włączamy nowy profil i wyłączamy stary przez @Primary
    @Primary
    @Profile("integration") //"!prod"}) // Tylko obowiązuje jeśli mamy określony profil, doprecyzowujemy że nie dla profilu produkcyjnego
    TaskRepository testRepo() {
        // Klasa anonimowa - jest problem, contextLoad() się nie uruchomi
        return new TaskRepository() {

            private final Map<Integer, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll () {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Page<Task> findAll ( final Pageable page){
                return null;
            }

            @Override
            public Optional<Task> findById ( final Integer id){
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public boolean existsById ( final Integer id){
                return tasks.containsKey(id);
            }

            @Override
            public Task save ( final Task entity){
                return tasks.put(tasks.size() + 1, entity);
            }

            @Override
            public List<Task> findByDone ( final boolean done){
                return null;
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id ( final Integer groupId){
                return false;
            }
        };
    }
}
