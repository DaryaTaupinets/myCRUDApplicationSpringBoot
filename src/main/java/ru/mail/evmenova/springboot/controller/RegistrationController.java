package ru.mail.evmenova.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mail.evmenova.springboot.model.Role;
import ru.mail.evmenova.springboot.model.User;
import ru.mail.evmenova.springboot.repository.UserRepository;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = userRepository.findUserByEmail(user.getEmail());
        if (userFromDb != null) {
            model.addAttribute("message", "User exists!!!");

        }
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";

    }
}
