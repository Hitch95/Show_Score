package com.example.show_score.restcontroller;

import com.example.show_score.model.ErrorBean;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Centralize exception handling for all controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

//    On transforme toutes les exceptions en ErrorBean retourné sous forme JSON
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorBean> handleGeneric(Exception ex, HttpServletRequest request) {
//        ErrorBean error = new ErrorBean(LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
//                "Une erreur interne est survenue",
//                request.getRequestURI(),
//                null
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//  On peut appliquer un traitement spécifique pour certaine exception, ici celle du validator
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBean> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorBean error = new ErrorBean(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                "Invalid request body",
                request.getRequestURI(),
                errors
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorBean> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        ErrorBean error = new ErrorBean(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}