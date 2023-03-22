package ru.job4j.job4j_auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.job4j_auth.domain.Person;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();

}
