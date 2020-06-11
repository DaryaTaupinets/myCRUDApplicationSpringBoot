package ru.mail.evmenova.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mail.evmenova.springboot.model.Role;
import ru.mail.evmenova.springboot.model.User;
import ru.mail.evmenova.springboot.repository.UserRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMiN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUser(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<User> users;
        if (filter != null && !filter.isEmpty()) {
            users = userRepository.findByFirstName(filter);
        } else {
            users = userRepository.findAll();
        }
        model.addAttribute("users", users);
        model.addAttribute("filter", filter);
        return "admin-page";
    }


    @GetMapping("/update")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user){
            user.setEmail(username);
            Set<String> roles = Arrays.stream(Role.values())
                    .map(Role::name)
                    .collect(Collectors.toSet());
            user.getRoles().clear();
            for (String key : form.keySet()){
                if (roles.contains(key)){
                    user.getRoles().add(Role.valueOf(key));
                }
        }
            userRepository.save(user);
        return "redirect:/user";
    }



    @GetMapping(value = "/create")
    public String formAddUser() {
        return "new-user";
    }


    @PostMapping("/create")
    public String addUser(@RequestParam String firstName,
                      @RequestParam String lastName,
                      @RequestParam Byte age,
                      @RequestParam String email,
                      @RequestParam String password,
                      Model model) {
        User user = new User(firstName, lastName, age, email, password);
        userRepository.save(user);
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "redirect:/user";
    }
}