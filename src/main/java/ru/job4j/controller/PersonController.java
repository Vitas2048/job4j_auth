package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;
import ru.job4j.service.PersonService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService persons;

    @GetMapping("/")
    public List<Person> findAll() {
        return persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = persons.findById(id);
        if (person.isEmpty()) {
            throw new NoSuchElementException("person not found");
        }
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Login or password is empty");
        }
        return persons.save(person) == null
                ? ResponseEntity.ok().build()
                : new ResponseEntity<Person>(persons.save(person), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Login or password is empty");
        }
           return persons.save(person) == null
                   ? ResponseEntity.ok().build()
                   : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        var res = persons.findById(id);
        if (res.isEmpty()) {
            throw new NoSuchElementException("person not found");
        }
        persons.delete(person);
        return ResponseEntity.ok().build();

    }
}
