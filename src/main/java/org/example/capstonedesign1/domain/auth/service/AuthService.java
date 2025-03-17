package org.example.capstonedesign1.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.example.capstonedesign1.domain.auth.dto.request.SignUpCompleteRequest;
import org.example.capstonedesign1.domain.auth.dto.request.SignUpRequest;
import org.example.capstonedesign1.domain.auth.dto.response.SignUpResponse;
import org.example.capstonedesign1.domain.auth.jwt.JwtUtil;
import org.example.capstonedesign1.domain.user.entity.Profile;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.entity.enums.Status;
import org.example.capstonedesign1.domain.user.repository.UserRepository;
import org.example.capstonedesign1.global.exception.ConflictException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public SignUpResponse signUp(SignUpRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        }

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        String accessToken = tokenService.issueAccessToken(user.getId().toString());
        return new SignUpResponse(accessToken, user.isRegisterCompleted());
    }


}
