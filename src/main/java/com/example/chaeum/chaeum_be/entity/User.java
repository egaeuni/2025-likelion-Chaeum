package com.example.chaeum.chaeum_be.entity;

import jakarta.validation.constraints.Email;
import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 이름
    private String name;
    // 전화번호
    @Column(unique = true)
    private String phoneNum;
    //이메일
    @Column(nullable = false, unique = true)
    @Email(message = "이메일 형식이 아닙니다.")
    private String  email;
    // 비밀번호
    @Column(nullable = false)
    private String password;

    @Builder.Default
    private boolean isFirstLogin = true;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserPreference userPreference;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setUserPreference(UserPreference preference) {
        this.userPreference = preference;
        preference.setUser(this);
    }
}
