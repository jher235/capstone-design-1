package org.example.capstonedesign1.domain.user.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Status {

    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    DELETED("삭제"),
    SIGNUP_INCOMPLETE("회원가입 미완료")
    ;

    private final String description;

}

