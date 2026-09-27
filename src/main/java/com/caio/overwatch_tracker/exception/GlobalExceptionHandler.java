package com.caio.overwatch_tracker.exception;

import com.caio.overwatch_tracker.hero.HeroNotFoundException;
import com.caio.overwatch_tracker.match.MatchNotFoundException;
import com.caio.overwatch_tracker.performance.PerformanceDoesNotBelongToMatchException;
import com.caio.overwatch_tracker.performance.PerformanceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HeroNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHeroNotFound(
            HeroNotFoundException exception,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMatchNotFound(
            MatchNotFoundException exception,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(PerformanceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePerformanceNotFound(
            PerformanceNotFoundException exception,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(PerformanceDoesNotBelongToMatchException.class)
    public ResponseEntity<ErrorResponse> handlePerformanceDoesNotBelongToMatch(
            PerformanceDoesNotBelongToMatchException exception,
            HttpServletRequest request) {

        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }
}