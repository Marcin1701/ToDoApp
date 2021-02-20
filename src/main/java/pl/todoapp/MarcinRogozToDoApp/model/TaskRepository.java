package pl.todoapp.MarcinRogozToDoApp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Klasa służy do komunikacji z bazą danych
//     Jest to interfejs        Rozszerza             1. Jaka encja 2. Jaki typ klucza
// Udostępnienie repozytorium
// RepositoryRestResource() -> z nawiasami to HATEOS
@RepositoryRestResource(path = "todos")
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // Jpa repository - mamy operacja findAll
    // mamy sortowanie itp
    // Mamy operację CRUD
    // Jak sprawdzić? - PPM na JpaRepository - Goto - super class

    // Wystarczy interfejs bazowy, żeby udostępnić wszystkie operacje

    // HATEOS
    // Stan aplikacji reprezentowane poprzez hipermedia, informacje otrzymywane przez _ (podkreślenie)
    // Np. _links, _embedded

}
