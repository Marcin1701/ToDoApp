package pl.todoapp.MarcinRogozToDoApp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

// Klasa służy do komunikacji z bazą danych
//     Jest to interfejs        Rozszerza             1. Jaka encja 2. Jaki typ klucza
// Udostępnienie repozytorium
// RepositoryRestResource() -> z nawiasami to HATEOS
// 1 parametr to url gdzie jest dostępny, a 2 to kolekcja (Tasks)
//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos")
@RepositoryRestResource
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // Co jeśli nie chcemy mieć metod CRUDowych np delete lub save
    // Nadpisujemy metody, czyli @RestResource(exported = false)
    // NADPISUJEMY OBIE METODY DELETE
    // Skutkiem jest METHOD NOT ALLOWED - Błąd 405
    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Task task);


    // Jpa repository - mamy operacja findAll
    // mamy sortowanie itp
    // Mamy operację CRUD
    // Jak sprawdzić? - PPM na JpaRepository - Goto - super class

    // Wystarczy interfejs bazowy, żeby udostępnić wszystkie operacje

    // HATEOS
    // Stan aplikacji reprezentowane poprzez hipermedia, informacje otrzymywane przez _ (podkreślenie)
    // Np. _links, _embedded

}
