package com.example.show_score.restcontroller;

import com.example.show_score.dto.LoginResponse;
import com.example.show_score.model.UserBean;
import com.example.show_score.service.JwtService;
import com.example.show_score.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Auth API", description = "API for authentication")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    // http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login attempt for: " + loginRequest.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            final String token = jwtService.generateToken(userDetails);

            UserBean user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

            System.out.println("Authentication successful for: " + loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(token, user));

        } catch (BadCredentialsException ex) {
            System.out.println("Authentication failed: Bad credentials");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // http://localhost:8080/api/auth/user
    @GetMapping("/user")
    public ResponseEntity<UserBean> getCurrentUser() {
        UserBean user = userService.findById(1L);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Getter
    public static class LoginRequest {
        private String username;
        private String password;

        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
    }
}