package com.bankingsystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankingsystem.dto.AuthResponse;
import com.bankingsystem.dto.PageResponse;
import com.bankingsystem.dto.UserResponse;
import com.bankingsystem.entity.RefreshToken;
import com.bankingsystem.entity.Role;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.UserRepository;
import com.bankingsystem.service.AuditService;
import com.bankingsystem.service.RefreshTokenService;
import com.bankingsystem.service.UserService;
import com.bankingsystem.util.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // Added: SLF4J Logger (30-06-2026)
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuditService auditService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {

        User savedUser = userService.register(user);

        log.info("User registered successfully: {}", savedUser.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    // LOGIN
 // Modified: Login with Refresh Token (01-07-2026)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {

        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean match = passwordEncoder.matches(
                user.getPassword(),
                existingUser.getPassword());

        if (!match) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(
                existingUser.getEmail(),
                existingUser.getRole().name());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(existingUser);

        auditService.saveLog(existingUser.getEmail(), "LOGIN");

        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken.getToken()
                )
        );
    }
    
 // Added: Refresh Access Token API (01-07-2026)
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestParam String refreshToken) {

        RefreshToken token =
                refreshTokenService.verifyRefreshToken(refreshToken);

        User user = token.getUser();

        String accessToken = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name());

        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken
                )
        );
    }

 // Modified: Pagination + Dynamic Sorting (01-07-2026)
    @GetMapping("/users")
    public PageResponse<UserResponse> getAllUsers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return userService.getAllUsers(
                page,
                size,
                sortBy,
                direction);
    }

    // Added: Update User API (30-06-2026)
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id,
                           @Valid @RequestBody User user) {

        log.info("Update User API called for id {}", id);

        return userService.updateUser(id, user);
    }

    // Added: Get User By Id API (30-06-2026)
    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }

    // Added: Delete User API (30-06-2026)
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {

        log.info("Delete User API called for id {}", id);

        return userService.deleteUser(id);
    }
    
 // Added: Search User By Name API (01-07-2026)
    @GetMapping("/users/search/name")
    public List<UserResponse> searchUserByName(
            @RequestParam String name) {

        log.info("Search User By Name API | name={}", name);

        return userService.searchUserByName(name);
    }

    // Added: Search User By Role API (01-07-2026)
    @GetMapping("/users/search/role")
    public List<UserResponse> searchUserByRole(
            @RequestParam Role role) {

        log.info("Search User By Role API | role={}", role);

        return userService.searchUserByRole(role);
    }

}