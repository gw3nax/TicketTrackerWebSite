package ru.gw3nax.tickettrackerwebsite.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.FlightNotFoundException;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.UserNotAuthenticatedException;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.UserNotFoundException;

@ControllerAdvice
@Slf4j
public class ErrorResponseWrapper {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, Model model) {
        log.error("User not found: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error_page";
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public String handleFlightNotFoundException(FlightNotFoundException ex, Model model) {
        log.error("Flight not found: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error_page";
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public String handleUserNotAuthenticatedException(UserNotAuthenticatedException ex, Model model) {
        log.error("User not authenticated: {}", ex.getMessage());
        model.addAttribute("errorMessage", "You must be authenticated to access this resource.");
        return "error_page";
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpectedError(Exception ex, Model model) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "An unexpected error has occurred. Please try again later.");
        return "error_page";
    }
}
