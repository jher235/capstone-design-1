package org.example.capstonedesign1.domain.user.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.capstonedesign1.domain.user.entity.enums.Gender;
import org.example.capstonedesign1.domain.user.entity.enums.Status;

import java.time.LocalDate;

@Embeddable
public class Profile {
    private String nickname;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Status status;

    private Long asset;
    private Integer salary;
}
