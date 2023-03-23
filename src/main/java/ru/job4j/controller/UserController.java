package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> signUp(@RequestBody Person person) throws Exception {
        if ("con".equals(person.getUsername())) {
            throw new Exception("You are not Bill Gates");
        }
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Login or password is empty");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        persons.save(person);
        var body = new HashMap<>() {{
            put("key", "value");
        }}.toString();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Registration", "successful")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @GetMapping("/all")
    public ResponseEntity<String> findAll() {
        var map = new HashMap<>();
        persons.findAll().forEach(p -> map.put(p.getId(), p.getUsername()));
        var body = map.toString();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("List of Person", "successful")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);

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
