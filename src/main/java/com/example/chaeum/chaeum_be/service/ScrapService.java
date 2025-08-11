package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.entity.User;
import org.springframework.http.ResponseEntity;

public interface ScrapService {
    ResponseEntity<?> addScrap(User user, Long houseId);
    ResponseEntity<?> removeScrap(User user, Long houseId);
    ResponseEntity<?> myscrap(User user);

}
