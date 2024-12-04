package com.example.alamnibackend.controllers;

import com.example.alamnibackend.models.ERole;
import com.example.alamnibackend.models.Role;
import com.example.alamnibackend.models.User;
import com.example.alamnibackend.payload.response.MessageResponse;
import com.example.alamnibackend.repository.RoleRepository;
import com.example.alamnibackend.repository.UserRepository;
import com.example.alamnibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        System.out.println("Get all users");
        return userRepository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageResponse("User deleted successfully."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable String id, @RequestBody List<String> roleNames) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
        user.getRoles().clear();
        for (String roleName : roleNames) {
            Optional<Role> role = roleRepository.findByName(ERole.valueOf(roleName));
            if (role.isPresent()) {
                user.getRoles().add(role.get());
            }
        }
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User roles updated successfully."));
    }
}