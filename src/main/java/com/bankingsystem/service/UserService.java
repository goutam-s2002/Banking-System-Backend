package com.bankingsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankingsystem.dto.PageResponse;
import com.bankingsystem.dto.UserResponse;
import com.bankingsystem.entity.Role;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final AuditService auditService;

    // Added: SLF4J Logger (30-06-2026)
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Added: User Registration Logging (30-06-2026)
    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        auditService.saveLog(savedUser.getEmail(), "USER REGISTERED");

        log.info("User registered successfully: {}", savedUser.getEmail());

        return savedUser;
    }

 // Modified: Pagination + Dynamic Sorting (01-07-2026)
    public PageResponse<UserResponse> getAllUsers(
            int page,
            int size,
            String sortBy,
            String direction) {

        log.info("Fetching users | page={} size={} sortBy={} direction={}",
                page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users = userRepository.findAll(pageable);

        List<UserResponse> list = users.getContent()
                .stream()
                .map(user -> {

                    UserResponse response = new UserResponse();

                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());

                    return response;

                }).toList();

        PageResponse<UserResponse> response = new PageResponse<>();

        response.setContent(list);
        response.setPage(users.getNumber());
        response.setSize(users.getSize());
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        response.setLast(users.isLast());

        return response;
    }

    // Added: Update User Logging (30-06-2026)
    public User updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        User savedUser = userRepository.save(existingUser);

        log.info("User updated successfully: id={}", id);

        return savedUser;
    }

    // Added: Get User By Id API (30-06-2026)
    public UserResponse getUserById(Long id) {

        log.info("Fetching user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    // Added: Delete User API (30-06-2026)
    public String deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
        auditService.saveLog(user.getEmail(), "USER DELETED");

        log.info("User deleted successfully: id={}", id);

        return "User Deleted Successfully";
    }
    
 // Added: Search User By Name (01-07-2026)
    public List<UserResponse> searchUserByName(String name) {

        log.info("Searching users by name={}", name);

        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(user -> {

                    UserResponse response = new UserResponse();

                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());

                    return response;

                }).toList();
    }

    // Added: Search User By Role (01-07-2026)
    public List<UserResponse> searchUserByRole(Role role) {

        log.info("Searching users by role={}", role);

        return userRepository.findByRole(role)
                .stream()
                .map(user -> {

                    UserResponse response = new UserResponse();

                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());

                    return response;

                }).toList();
    }
}