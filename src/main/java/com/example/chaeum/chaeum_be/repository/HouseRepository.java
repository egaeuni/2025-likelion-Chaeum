package com.example.chaeum.chaeum_be.repository;

import com.example.chaeum.chaeum_be.entity.House;
import com.example.chaeum.chaeum_be.enums.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByOwnerId(Long ownerId);
    Optional<House> findById(Long id);
    
    // 공공 API 이식용
    Optional<House> findBySourceAndExternalId(SourceType source, String externalId);
    boolean existsBySourceAndExternalId(SourceType source, String externalId);
}
