package com.example.chaeum.chaeum_be.dto.user;

import com.example.chaeum.chaeum_be.entity.UserPreference;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OnboardingRequestDTO {
    private String email;

    @NotNull
    private UserPreference.PurposeType purpose;
    // BUY일 경우만 채워짐
    private List<UserPreference.UsagePurposeType> usagePurpose;    // 기타 의견
    private String additionalDetail;
}
