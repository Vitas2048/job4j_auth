package ru.job4j.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;

@ControllerAdvice
@AllArgsConstructor
public class Validation {

    private ObjectMapper mapper;

    @ExceptionHandler(value = {NullPointerException.class})
    public void handleException(Exception e, HttpServletResponse res, HttpServletRequest req) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(mapper.writeValueAsString(new HashMap<>() { {
            put("message", "Some fields are empty");
            put("details", e.getMessage());
        }}));
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public void handleEmptyException(Exception e, HttpServletResponse res, HttpServletRequest req) throws IOException {
        res.setStatus(HttpStatus.NOT_FOUND.value());
        res.setContentType("application/json");
        res.getWriter().write(mapper.writeValueAsString(new HashMap<>() { {
            put("message", "not found");
            put("details", e.getMessage());
        }}));
    }

}
