package pl.todoapp.MarcinRogozToDoApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Zmuszamy do działania
@Component
// Logger z interceptorem                                       stare
public class LoggerInterceptor implements HandlerInterceptor {//extends HandlerInterceptorAdapter {

    // Metody do nadpisania
    // Przed wejściem do kontrolera
    // Po wejściu do kontrolera
    // Po zakończeniu procesowania - już w widoku

    // Logger
    public static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    // Nie można zmienić requesta i responsa!
    // Wywołany jako 2
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        // Informacja analogiczna co jest w filtrze
        logger.info("[preHandle]" + request.getMethod() + " " + request.getRequestURI());
        // Zwracamy true to dojdzie do procesowania dalej
        // Jaka będzie kolejność filtrów
        return true;
    }

    // Po kontrolerze
    // Wywołany jako 3
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        logger.info("[postHandle]");
    }
}
