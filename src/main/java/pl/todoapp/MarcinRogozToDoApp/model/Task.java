package pl.todoapp.MarcinRogozToDoApp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

// Encje są zarządzane przez enitity managera
// Metody persist itd

// Klasa modelu Zadań
@Entity                 // Encja w bazie danych
@Table(name = "tasks")  // Dodawanie tabeli
public class Task {

    // Wymagany klucz w bazie danych
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // adnotacja która generuje automatycznie klucze
    private int id; // Nadawane wartości proste
    // Adnotacje walidujące javax.validation
    @NotBlank(message = "Tasks description must not be empty")
    // Nazwa inaczej w bazie, albo mapujemy pola albo na getterach
    private String description;

    private boolean done;

    // UWAGA WAŻNE!!!!
    // W DB kolumny często są nazywane z podkreśleniami: surname_and_name
    // Tabele są w liczbie mnogiej
    // Spring mapuje takie nazwy do camel case: surnameAndName

    // Format daty do postmana "2020-12-23T23:59:59.999"
    private LocalDateTime deadline;

    /*
    // Tych pól nie udostępniamy w JSON
    // Kolumna kiedy task stworzyliśmy\
    // Ciekawostka: jest adnotacja polska @PESEL
    // Jeśli nie chcemy mapować zmiennej na kolumnę w bazie dajemy: @Transient
    private LocalDateTime createdOn;
    // Kiedy zaktualizowana
    private LocalDateTime updatedOn;
    */

    // Wstawiamy obiekt który jest embeddable
    // Możemy zmapować w tym miejscu jakie tabele bierzemy z DB
    // Dajemy @AttributeOverride() lub @AttributeOverrides()
    //@AttributeOverrides({
    //            @AttributeOverride(column = @Column(name="updatedOn"), name="updatedOn")
    //})
    @Embedded
    private Audit audit = new Audit();

    // Nowy obiekt wskazujący na inną encję TaskGroup
    // Wiele tasków może trafić do 1 grupy @ManyToOne
    // @ManyToOne(cascade = CascadeType.REMOVE) - jeśli usuwa się task to usuwa się cała grupa
    // Jeśli dodajemy grupę, która ma wiele tasków - dodajemy taska - aktualizujemy grupę
    @ManyToOne
    // Grupę dociągamy jeśli jest potrzebna - pobieramy taska - pobieramy grupę
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    Task() {
    }

    public Task(String description, LocalDateTime deadline){
        this.description = description;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getGroup() {
        return group;
    }

    // Kiedy zczytamy encję
    public void updateFrom(final Task source) {
        this.description = source.description;
        this.done = source.done;
        this.deadline = source.deadline;
        this.group = source.group;
    }

    /*
    // Funkcja odpali się przed zapisem do DB - zazwyczaj stosowana do insertów
    @PrePersist
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    // Wykonywana przed dokładaniem encji do managera
    @PreUpdate
    void preMerge() {
        updatedOn = LocalDateTime.now();
    }
    */
}
