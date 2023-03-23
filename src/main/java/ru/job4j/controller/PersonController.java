package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.domain.marker.Operation;
import ru.job4j.repository.PersonRepository;
import ru.job4j.service.PersonService;

import javax.validation.Valid;
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
    @Validated({Operation.OnCreate.class})
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Login or password is empty");
        }
        return persons.save(person) == null
                ? ResponseEntity.ok().build()
                : new ResponseEntity<Person>(persons.save(person), HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Validated({Operation.OnUpdate.class})
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Login or password is empty");
        }
           return persons.save(person) == null
                   ? ResponseEntity.internalServerError().build()
                   : ResponseEntity.ok().build();
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
