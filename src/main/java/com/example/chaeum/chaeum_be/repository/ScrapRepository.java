package com.example.chaeum.chaeum_be.repository;

import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.entity.House;
import com.example.chaeum.chaeum_be.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository <Scrap, Long> {
    boolean existsByUserAndHouse(User user, House house);
    List<Scrap> findByUser(User user);
    void deleteByUserAndHouse(User user, House house);
    boolean existsByUserIdAndHouseId(Long userId, Long houseId);
}
