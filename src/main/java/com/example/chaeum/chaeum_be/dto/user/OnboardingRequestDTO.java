package com.example.chaeum.chaeum_be.dto.user;

import com.example.chaeum.chaeum_be.enums.PurposeType;
import com.example.chaeum.chaeum_be.enums.UsagePurposeType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OnboardingRequestDTO {
    private String email;

    @NotNull
    private PurposeType purpose;
    // BUY일 경우만 채워짐
    private List<UsagePurposeType> usagePurpose;    // 기타 의견

    private String additionalDetail;
    private String usagePurposeEtcDetail;

    @AssertTrue(message = "매수 목적(BUY)일 때는 활용 목적을 하나 이상 선택해 주세요.")
    public boolean isUsagePurposeRequiredWhenBuy() {
        if (purpose == null) return true; // 다른 검증에서 잡힘
        if (purpose.name().equalsIgnoreCase("BUY")) {
            return usagePurpose != null && !usagePurpose.isEmpty();
        }
        return true;
    }

    @AssertTrue(message = "기타(ETC)를 선택하셨다면 상세 내용을 입력해 주세요.")
    public boolean isEtcDetailRequiredWhenEtcSelected() {
        if (usagePurpose == null) return true;
        boolean hasEtc = usagePurpose.contains(UsagePurposeType.ETC);
        return !hasEtc || (usagePurposeEtcDetail != null && !usagePurposeEtcDetail.isBlank());
    }
}
