package org.example.capstonedesign1.domain.auth.jwt;

import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
public class BearerEncoder {
    private static final String TOKEN_TYPE = "Bearer ";

    public static String encode(String token) {
        return URLEncoder.encode(TOKEN_TYPE + token, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    public static String decode(String encodedToken) {
        return URLDecoder.decode(encodedToken, StandardCharsets.UTF_8).replace("Bearer ", "");
    }
}