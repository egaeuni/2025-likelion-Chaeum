package com.example.chaeum.chaeum_be.repository;

import com.example.chaeum.chaeum_be.entity.PublicHouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicHouseRepository extends JpaRepository<PublicHouse, Long> {
    boolean existsByAddressAndSaleType(String address, String saleType);
}