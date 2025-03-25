package org.example.capstonedesign1.domain.user.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.example.capstonedesign1.domain.auth.dto.request.SignUpCompleteRequest;
import org.example.capstonedesign1.domain.user.entity.enums.Gender;
import org.example.capstonedesign1.domain.user.entity.enums.Status;

import java.time.LocalDate;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {
    private String nickname;
    private LocalDate birthDate;
    private Long asset;
    private Integer salary;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Status status;

    private String propensity;

    public static Profile from(SignUpCompleteRequest request){
        return Profile.builder()
                .nickname(request.getNickname())
                .gender(request.getGender())
                .salary(request.getSalary())
                .birthDate(request.getBirthDate())
                .asset(request.getAsset())
                .status(Status.ACTIVE)
                .build();
    }

    public void updatePropensity(String propensity){
        this.propensity = propensity;
    }

}
