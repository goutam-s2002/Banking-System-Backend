package com.bankingsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankingsystem.entity.Role;
import com.bankingsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Added: Search User By Name (01-07-2026)
    List<User> findByNameContainingIgnoreCase(String name);

    // Added: Search User By Role (01-07-2026)
    List<User> findByRole(Role role);

}