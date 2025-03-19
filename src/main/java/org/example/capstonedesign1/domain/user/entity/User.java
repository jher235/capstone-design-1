package org.example.capstonedesign1.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.capstonedesign1.domain.user.entity.enums.Role;
import org.example.capstonedesign1.global.common.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean registerCompleted = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Embedded
    private Profile profile;

    public User(String email, String password){
        this.email = email;
        this.password = password;
        this.registerCompleted = false;
    }

    public void signUpComplete(Profile profile){
        this.profile = profile;
        this.registerCompleted = true;
    }

}
