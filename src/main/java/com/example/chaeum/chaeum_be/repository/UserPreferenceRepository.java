package com.example.chaeum.chaeum_be.repository;

import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByUser(User user);
}