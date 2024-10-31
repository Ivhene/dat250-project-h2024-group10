package no.hvl.dat250.pollApp.controller;

import no.hvl.dat250.pollApp.entity.User;
import no.hvl.dat250.pollApp.repo.DomainManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private DomainManager domainManager;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = domainManager.getAllUsers().stream().toList();

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user, HttpServletResponse response) {
        User registeredUser = domainManager.addUser(user);

        Cookie userCookie = new Cookie("username", user.getUsername());
        userCookie.setMaxAge(7 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);

        return ResponseEntity.status(201).body(registeredUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = domainManager.getUserById(userId); // userId is username

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        User updatedUser = domainManager.updateUser(userId, user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable String userId) {
        domainManager.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
