package pl.todoapp.MarcinRogozToDoApp.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// Klasa obsługuje aspekty
// Jest zarejestrowana w springu
@Aspect
@Component
class LogicAspect {
    // Tworzymy timer
    private final Timer projectCreateGroupTimer;

    // Do timera konstruktur - Timer pobieramy z MeterRegistry
    // Spring wstrzykuje zależność
    LogicAspect(final MeterRegistry registry) {
        projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    // Różne aspekty
    // Zapinamy się dookoła createGroup
    // ProceedingJointPoint to punkt łączenia z logiką
    // Nie przenosimy logiki do aspektów, raczej niefunkcjonalne rzeczy, logowanie, metryki
    // Gwiazdka oznacza dowolny typ zwracany - dwie kropki to jakiekolwiek parametry
    // Można dawać && @annotation(Override) - precyzować dokładnie jakie metody nas interesują ( z adnotacją override)
    @Around("execution(* pl.todoapp.MarcinRogozToDoApp.logic.ProjectService.createGroup(..))") // Coś co pozwala nam zatrzymać się wywołanie dookoła którego przygotujemy logikę np dodajemy wartość do cache

    // 1. Zatrzymujemy się na chwilę przed wykonaniem metody
    // 2. Dodajemy sobie w tym przypadku Timer
    // 3. Coś sobie mierzymy
    // 4. W razie czego mamy dalszą obsługę wyjątków

    // DLA WYWOLANIA CREATEGROUP TO CO PONIŻEJ MA SIĘ WYKONAć
    Object aroundProjectCreateGroup(ProceedingJoinPoint jp) {
        // Dodajemy timer
        return projectCreateGroupTimer.record(() -> {
        // Pozwalamy na pójście dalej i coś zwracamy
            try {
                // Idziemy dalej w tym punkcie
                return jp.proceed();
            } catch (Throwable e) {
                // Np jeśli się nie powiedzie to rzucamy wyjątek
                if (e instanceof RuntimeException){
                    throw (RuntimeException)e;
                }
                throw new RuntimeException(e);
            }
        });
    }

}
