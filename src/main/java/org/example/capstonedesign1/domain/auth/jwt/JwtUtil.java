package org.example.capstonedesign1.domain.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String TOKEN_TYPE = "Bearer ";
    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtUtil(@Value("${jwt.access-token.secret}") final String secretKey,
                   @Value("${jwt.access-token.expired-time}") final long validityInMilliseconds){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String issueAccessToken(String subject){
        String accessToken = createAccessToken(subject);
        return encodeJwtToken(accessToken);
    }

    public String createAccessToken(String subject){
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String encodeJwtToken(String token){
        String bearerToken = TOKEN_TYPE + token;
        return URLEncoder.encode(bearerToken, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

}
