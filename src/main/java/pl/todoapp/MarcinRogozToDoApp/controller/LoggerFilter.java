package pl.todoapp.MarcinRogozToDoApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// Adnotacja order
@Order(Ordered.HIGHEST_PRECEDENCE)
// Filtr
@Component
public class LoggerFilter implements Filter {//, Ordered {

    // Dodanie logowania
    public static final Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

    // Metoda pobiera 3 parametry
    // Metoda sprawdza requesty
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        // Jeżli chcemy przeprocesować request - na odpowiedź warto poczekać
        // Trzeba by złapać wyjątek który może polecieć
        // W Springu jest alternatywa HandletInterceptor
        if (request instanceof HttpServletRequest) {
            var httpRequest = (HttpServletRequest) request;
            // Jak ktoś uderza to dostanie ten filtr jako pierwszy
            // Wywołany jako 1
            logger.info("[doFilter]" + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
        }
        // Aplikacja bez chain zawiera buga - request nie jest procesowany.
        // W konsoli są logowane requesty, ale po filtrowaniach spring nie poszedł dalej
        // Po ifie żeby każde żądanie poleciało
        // Możemy przekazywać parametry requesta!, modyfikować je
        chain.doFilter(request, response);
        // Wywołany jako 4
        logger.info("[doFilter] 2");
    }

    // Definiowanie kolejności filtrów - Ordered
    // Zależą od zwracanej wartości - wdg ważności 0 - najbardziej 1 mniej itd
    //@Override
    //public int getOrder() {
    //    return Ordered.HIGHEST_PRECEDENCE;
    //}
}
