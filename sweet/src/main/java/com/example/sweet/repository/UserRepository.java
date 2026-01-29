package com.example.sweet.repository;

import com.example.sweet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // ✅ NEW: Fetch user profile safely
    Optional<User> findById(Long id);
}
