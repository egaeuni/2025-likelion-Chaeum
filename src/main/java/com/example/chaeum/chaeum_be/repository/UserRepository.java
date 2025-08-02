package com.example.chaeum.chaeum_be.repository;

import com.example.chaeum.chaeum_be.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNum(String phoneNum);
    Optional<User> findUserByEmail(String email);
}
