package com.example.chaeum.chaeum_be.dto.user;

import com.example.chaeum.chaeum_be.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private String email;

    public UserResponseDTO(User user) {
        if (user != null) {
            this.email = user.getEmail();
        }
    }
}
