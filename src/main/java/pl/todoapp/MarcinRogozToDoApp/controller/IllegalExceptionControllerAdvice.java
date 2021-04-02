package pl.todoapp.MarcinRogozToDoApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Adnotacja pozwala "zapiąć się" na obsługę błędów z kontrolerów
// Obsługuje exception handler
// Jest też RestControllerAdvice
// Możemy powiedziec do jakich typów, klas, pakietów ma obsługiwać kontroler
// Oznacza to że ten kontroler obsłuży wyjątki z oznaczonych kontrolerów - jest to aspekt kontrolerów (szczegół)
@RestControllerAdvice(annotations = IllegalExceptionProcessing.class)   // Własna adnotacja
public class IllegalExceptionControllerAdvice { //Wszystkie kontrolery - każdy kontroler z którego polecy Illegal,
    // będzie miał obsługę wyniesioną do części wspólnej

    // Obie metody z TaskGroupController
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
