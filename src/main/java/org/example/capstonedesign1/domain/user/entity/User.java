package org.example.capstonedesign1.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.global.common.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;
    private String password;

    @Embedded
    private Profile profile;


}
