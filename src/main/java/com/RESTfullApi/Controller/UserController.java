package com.RESTfullApi.Controller;

import com.RESTfullApi.Entity.User;
import com.RESTfullApi.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping(path = "users")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userById = userService.getUserById(id);
        return userById.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable Long id,
                                           @RequestBody User user) {

        return userService.getUserById(id).map(
                user1 -> {
                    user1.setName(user.getName());
                    user1.setEmail(user.getEmail());
                    User updatedUser = userService.saveUser(user1);
                    return ResponseEntity.ok(updatedUser);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
