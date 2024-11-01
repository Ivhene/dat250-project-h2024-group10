package no.hvl.dat250.pollApp.controller;

import jakarta.servlet.http.HttpServletRequest;
import no.hvl.dat250.pollApp.entity.User;
import no.hvl.dat250.pollApp.repo.DomainManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<User> addUser(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
        User registeredUser = domainManager.addUser(user);

        Cookie userCookie = getCookie(user.getUsername(), request);

        if (userCookie == null) {
            userCookie = new Cookie("username", user.getUsername());
        }
        userCookie.setMaxAge(7 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);

        return ResponseEntity.status(201).body(registeredUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId, HttpServletResponse response, HttpServletRequest request) {
        User user = domainManager.getUserById(userId);// userId is username

        Cookie userCookie = getCookie(user.getUsername(), request);

        if (userCookie == null) {
            userCookie = new Cookie("username", user.getUsername());
        }
        userCookie.setMaxAge(7 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);

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

    @GetMapping("/api/check-cookie")
    public ResponseEntity<String> checkCookie(@RequestParam String cookieName, HttpServletRequest request) {
        Cookie cookie = getCookie(cookieName, request);
        if (cookie != null) {
            return ResponseEntity.ok(cookie.getValue()); // Return the cookie value if it exists
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cookie not found");
        }
    }

    public Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) { // Check if cookies array is not null
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null; // Return null if the cookie is not found or no cookies are present
    }
}
