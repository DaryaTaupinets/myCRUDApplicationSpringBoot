package ru.mail.evmenova.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mail.evmenova.springboot.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByFirstName(String firstName);

    User findUserByEmail(String email);
}
