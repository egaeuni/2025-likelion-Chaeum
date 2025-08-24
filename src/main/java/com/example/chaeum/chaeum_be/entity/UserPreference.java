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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PurposeType purpose;

    @ElementCollection(targetClass = UsagePurposeType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_usage_purpose", joinColumns = @JoinColumn(name = "user_id"))
    private List<UsagePurposeType> usagePurpose;

    @Column(length = 500)
    private String additionalDetail;

    @Column(length = 500)
    private String usagePurposeEtcDetail;

    @jakarta.validation.constraints.AssertTrue(
            message = "저만의 희망사항이 있어요"
    )
    private boolean isEtcDetailValid() {
        boolean hasEtc = usagePurpose != null && usagePurpose.contains(UsagePurposeType.ETC);
        return !hasEtc || (usagePurposeEtcDetail != null && !usagePurposeEtcDetail.isBlank());
    }

    public void setUser(User user) {
        this.user = user;
        if (user.getUserPreference() != this) user.setUserPreference(this);
    }
}
