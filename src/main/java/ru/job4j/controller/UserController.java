package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private PersonService persons;

    private ObjectMapper objectMapper;

    private BCryptPasswordEncoder encoder;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) throws Exception {
        if ("con".equals(person.getUsername())) {
            throw new Exception("You are not Bill Gates");
        }
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Login or password is empty");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        persons.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return persons.findAll();
    }

    @ExceptionHandler(value = {Exception.class})
    public void exceptionHandler(Exception e, HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
    }
}
