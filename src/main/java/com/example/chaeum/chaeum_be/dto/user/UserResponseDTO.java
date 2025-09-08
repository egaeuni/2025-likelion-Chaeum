package com.example.chaeum.chaeum_be.dto.user;

import com.example.chaeum.chaeum_be.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String token;
    private Long id;
    private String name;
    private String email;

    @JsonProperty("isFirstLogin")
    private boolean isFirstLogin;

    public UserResponseDTO(User user) {
        if (user != null) {
            this.email = user.getEmail();
        }
    }
}

