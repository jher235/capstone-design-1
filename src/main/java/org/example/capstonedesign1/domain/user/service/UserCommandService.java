package org.example.capstonedesign1.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.propensity.entity.UserPropensity;
import org.example.capstonedesign1.domain.propensity.service.PropensityQueryService;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.repository.UserRepository;
import org.example.capstonedesign1.global.exception.AuthorizedException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final PropensityQueryService propensityQueryService;

    private final UserRepository userRepository;


    @Transactional
    public void updatePropensity(User user, UUID userPropensityId){
        UserPropensity userPropensity = propensityQueryService.findUserPropensityById(userPropensityId);
        if(!isSameUser(user, userPropensity)){
            throw new AuthorizedException(ErrorCode.UN_AUTHORIZED);
        }

        user.updatePropensity(userPropensity.getPropensity());
        userRepository.save(user);
    }


    private boolean isSameUser(User user, UserPropensity userPropensity){
        return userPropensity.getUser().equals(user);
    }
}
