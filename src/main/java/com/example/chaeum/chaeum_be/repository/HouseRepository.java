package com.example.chaeum.chaeum_be.repository;

import com.example.chaeum.chaeum_be.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByOwnerId(Long ownerId);
}
