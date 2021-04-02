package pl.todoapp.MarcinRogozToDoApp.controller;

import java.lang.annotation.*;

// Adnotacja ma specjalne przeznaczenia
// Pozwalają konfigurować własne adnotacje
// Na jakich elementach możemy stawiać adnotację
// W target value jest typu tablicy , można dawać po przecinku
// Adnotacja jest dostępna do typów class, interface, enum
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)    // Jak długo adnotacja ma pozostawać, domyślnie parametr leci do value
@interface IllegalExceptionProcessing {


}
