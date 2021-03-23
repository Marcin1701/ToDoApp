package pl.todoapp.MarcinRogozToDoApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import pl.todoapp.MarcinRogozToDoApp.model.Task;
import pl.todoapp.MarcinRogozToDoApp.model.TaskRepository;

import java.util.List;

// Klasa służy do komunikacji z bazą danych
//     Jest to interfejs        Rozszerza             1. Jaka encja 2. Jaki typ klucza
// Udostępnienie repozytorium
// RepositoryRestResource() -> z nawiasami to HATEOS
// 1 parametr to url gdzie jest dostępny, a 2 to kolekcja (Tasks)
//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos")
// Rezygnujemy ze spring data rest - repo nie potrzebuje udostępniać bezpośrendio do kontrolera
// @RepositoryRestResource
@Repository                                         // Jeśli rozszerza JPARepository - jest automatycznie beanem
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    // Tworzenie własnych zapytań SQL - nadpisywanie metod
    @Override
    // Można napisać z ?1 - jest to pierwszy parametr metody
    // @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=?1")
    // boolean existsById(Integer id);
    // Adnotacja @Param
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    // Generowanie zapytania sql
    // Żeby zamknąć grupę tasków - musimy mieć informację czy taski są zakończone
    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    // Jak sprytnie udostępnić kilka metod z repozytorium

    // Co jeśli nie chcemy mieć metod CRUDowych np delete lub save
    // Nadpisujemy metody, czyli @RestResource(exported = false)
    // NADPISUJEMY OBIE METODY DELETE
    // Skutkiem jest METHOD NOT ALLOWED - Błąd 405

    // Jeśli jest adnotacja @Repository to nie potrzebujemy metod poniżej:
    /*
    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);
    @Override
    @RestResource(exported = false)
    void delete(Task task);
    */

    // Repozytoria Spring (Spring Data) to DSL - procesowanie kolekcji
    // Każda metoda tłumaczona jest na zapytania do BD
    // Można tworzyć własne metody
    // Metoda zwraca listę tasków
    // Metoda będzie dostępna pod adresem URL
    // Dodajemy parametr @RestResource
    // Bez tego - mamy url /findByDoneIsTrue
    // W Postman - GET: localhost:8080/tasks/search/done
    // @RestResource(path = "done", rel = "done")
    // List<Task> findByDoneIsTrue();

    // Nie mamy wartości dla flagi done, musi zostać przekazana jako parametr
    // Musimy dodać parametr do adresu URL
    // W Postman - GET: localhost:8080/tasks/search/done2?state=false
    // @RestResource(path = "done2", rel="done2")
    // List<Task> findByDone(@Param("state") boolean done);


    // Jpa repository - mamy operacja findAll
    // mamy sortowanie itp
    // Mamy operację CRUD
    // Jak sprawdzić? - PPM na JpaRepository - Goto - super class

    // Wystarczy interfejs bazowy, żeby udostępnić wszystkie operacje

    // HATEOS
    // Stan aplikacji reprezentowane poprzez hipermedia, informacje otrzymywane przez _ (podkreślenie)
    // Np. _links, _embedded

}
