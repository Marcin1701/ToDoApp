package pl.todoapp.MarcinRogozToDoApp.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Klasa obsługuje aspekty
// Jest zarejestrowana w springu
@Aspect
@Component
class LogicAspect {

    public static final Logger logger = LoggerFactory.getLogger(LogicAspect.class);

    // Tworzymy timer
    private final Timer projectCreateGroupTimer;

    // Do timera konstruktur - Timer pobieramy z MeterRegistry
    // Spring wstrzykuje zależność
    LogicAspect(final MeterRegistry registry) {
        projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    @Pointcut("execution(* pl.todoapp.MarcinRogozToDoApp.logic.ProjectService.createGroup(..))")
    static void projectServiceCreateGroup() {
        // Ta metoda zwróci faktycznie punkt przecięcia - połączy sobie @Before i @Around
        // Jak sobie to wyniesiemy to trudniej zrobić literówkę
    }

    // Metoda wykonywana przed createGroup
    // Jest tutaj długi String - możemy to uwspólnić @Pointcut
    @Before("projectServiceCreateGroup()")
    void logMethodCall(JoinPoint jp) {
        // Połączyliśmy aspekt z metodą createGroup
        logger.info("Before {} with {}", jp.getSignature().getName(), jp.getArgs());
    }

    // Różne aspekty
    // Zapinamy się dookoła createGroup
    // ProceedingJointPoint to punkt łączenia z logiką
    // Nie przenosimy logiki do aspektów, raczej niefunkcjonalne rzeczy, logowanie, metryki
    // Gwiazdka oznacza dowolny typ zwracany - dwie kropki to jakiekolwiek parametry
    // Można dawać && @annotation(Override) - precyzować dokładnie jakie metody nas interesują ( z adnotacją override)
    @Around("projectServiceCreateGroup()") // Coś co pozwala nam zatrzymać się wywołanie dookoła którego przygotujemy logikę np dodajemy wartość do cache

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
