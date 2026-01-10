package com.example.onlineLibrary.web.controller;

import com.example.onlineLibrary.model.entity.Role;
import com.example.onlineLibrary.model.enums.RoleName;
import com.example.onlineLibrary.model.entity.User;
import com.example.onlineLibrary.service.RoleService;
import com.example.onlineLibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    // ==============================
    // PRIKAZ SVIH KORISNIKA (ADMIN CRUD)
    // ==============================
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/all-users";
    }

    // ==============================
    // FORMA ZA KREIRANJE NOVOG KORISNIKA (ADMIN)
    // ==============================
    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create-user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        // kodiranje password-a
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // default role USER
        Role userRole = roleService.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.setRoles(Collections.singleton(userRole));
        user.setActive(true);

        userService.save(user);
        return "redirect:/users";
    }

    // ==============================
    // BRISANJE KORISNIKA (ADMIN)
    // ==============================
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.findById(id).ifPresent(u -> userService.delete(u));
        return "redirect:/users";
    }

    // ==============================
    // REGISTRACIJA KORISNIKA (PUBLIC)
    // ==============================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleService.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.setRoles(Collections.singleton(userRole));
        user.setActive(true);
        userService.save(user);
        return "redirect:/users/login";
    }

    // ==============================
    // LOGIN (PUBLIC)
    // ==============================
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // ==============================
    // PO LOGIN-U
    // ==============================
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
