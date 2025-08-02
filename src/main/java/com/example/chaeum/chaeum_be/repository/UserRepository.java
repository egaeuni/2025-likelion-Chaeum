package com.example.chaeum.chaeum_be.repository;

import com.example.chaeum.chaeum_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNum(String phoneNum);
}
