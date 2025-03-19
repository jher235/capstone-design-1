package org.example.capstonedesign1.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.jwt.JwtProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;
    public String issueAccessToken(String subject){
        return jwtProvider.issueAccessToken(subject);
    }

}
