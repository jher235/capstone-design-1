package org.example.capstonedesign1.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.jwt.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;

    public String issueAccessToken(String subject){
        return jwtUtil.issueAccessToken(subject);
    }

}
