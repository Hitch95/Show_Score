package com.example.show_score.restcontroller;

import com.example.show_score.model.UserBean;
import com.example.show_score.service.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "API for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // http://localhost:8080/api/users
    @GetMapping
    public ArrayList<UserBean> getUsers() {
        return new ArrayList<>(userService.getAllUsers());
    }

    //http://localhost:8080/users/1
    @GetMapping("/{id}")
    public ResponseEntity<UserBean> getUserById(@PathVariable Long id) {
        UserBean userBean = userService.findById(id);
        if (userBean != null) {
            return ResponseEntity.ok(userBean);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //http://localhost:8080/users
    @PostMapping
    public ResponseEntity<UserBean> createUser(@Valid @RequestBody UserBean user) {
        UserBean savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    //http://localhost:8080/users/1
    @PutMapping("/{id}")
    public ResponseEntity<UserBean> updateUser(@PathVariable Long id, @Valid @RequestBody UserBean userDetails) {
        UserBean user = userService.findById(id);
        if (user != null) {
            userDetails.setId(id);
            UserBean updatedUser = userService.save(userDetails);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //http://localhost:8080/users/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        UserBean user = userService.getById(id);
        if (user != null) {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
