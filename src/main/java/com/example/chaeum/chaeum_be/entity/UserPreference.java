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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PurposeType purpose;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_preference_usage",
            joinColumns = @JoinColumn(name = "user_preference_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "usage_purpose", nullable = false)
    private List<UsagePurposeType> usagePurpose;

    @Column(length = 500)
    private String additionalDetail;

    @Column(length = 500)
    private String usagePurposeEtcDetail;

    @jakarta.validation.constraints.AssertTrue(message = "기타(ETC) 선택 시 상세 내용을 입력해 주세요.")
    public boolean isEtcDetailValid() {
        boolean hasEtc = usagePurpose != null && usagePurpose.contains(UsagePurposeType.ETC);
        return !hasEtc || (usagePurposeEtcDetail != null && !usagePurposeEtcDetail.isBlank());
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getUserPreference() != this) {
            user.setUserPreference(this);
        };
    }
}
