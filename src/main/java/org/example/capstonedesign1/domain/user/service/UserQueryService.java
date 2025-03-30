package org.example.capstonedesign1.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.domain.user.entity.Profile;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.repository.UserRepository;
import org.example.capstonedesign1.global.exception.BadRequestException;
import org.example.capstonedesign1.global.exception.NotFoundException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public User findById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public boolean isSameUser(User userA, User userB){
        return userA.equals(userB);
    }

    /**
     * user의 propensity가 필수적인 기능에서 propensity를 반환하기 위함.
     * @param user
     * @return
     */
    public Propensity ensureUserPropensity(User user){
        return Optional.ofNullable(user)
                .map(User::getProfile)
                .map(Profile::getPropensity)
                .orElseThrow(() -> new BadRequestException(ErrorCode.PROPENSITY_NOT_SET));
    }
}
