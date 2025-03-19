package org.example.capstonedesign1.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.dto.request.SignUpCompleteRequest;
import org.example.capstonedesign1.domain.auth.dto.request.SignUpRequest;
import org.example.capstonedesign1.domain.auth.dto.response.AuthenticationResponse;
import org.example.capstonedesign1.domain.user.entity.Profile;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.repository.UserRepository;
import org.example.capstonedesign1.global.exception.ConflictException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;


    /**
     * id, pw 회원가입
     * @param request
     */
    public AuthenticationResponse signUp(SignUpRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        }

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        String accessToken = tokenService.issueAccessToken(user.getId().toString());
        return new AuthenticationResponse(accessToken, user.isRegisterCompleted());
    }

    /**
     * 회원 정보 기입을 통해 회원가입 완료
     * @param user
     * @param request
     */
    @Transactional
    public void signUpComplete(User user, SignUpCompleteRequest request){
        if(user.isRegisterCompleted()){
           throw new ConflictException(ErrorCode.ALREADY_REGISTER_COMPLETED);
        }
        Profile profile = Profile.from(request);
        user.signUpComplete(profile);
        userRepository.save(user);
    }
}
