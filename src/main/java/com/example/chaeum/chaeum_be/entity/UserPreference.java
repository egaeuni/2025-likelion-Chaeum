package com.example.chaeum.chaeum_be.entity;

import com.example.chaeum.chaeum_be.enums.PurposeType;
import com.example.chaeum.chaeum_be.enums.UsagePurposeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPreference {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PurposeType purpose;

    @ElementCollection(targetClass = UsagePurposeType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_usage_purpose", joinColumns = @JoinColumn(name = "user_id"))
    private List<UsagePurposeType> usagePurpose;

    private String additionalDetail;
}
