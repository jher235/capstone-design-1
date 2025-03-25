package org.example.capstonedesign1.domain.propensity.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.common.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPropensity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false) // 굳이 연관관계 매핑을 하지 않아도 무방하다고 판단.
//    Propensity propensity;

    @Column(nullable = false)
    String propensity;

    @Column(nullable = false, columnDefinition = "JSON")
    String content;
}
