// com/example/chaeum/chaeum_be/service/MainService.java
package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.main.MainSectionResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;

public interface MainService {
    MainSectionResponseDTO loadMain(User me);
}
