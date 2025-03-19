package org.example.capstonedesign1.domain.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.auth.jwt.BearerEncoder;
import org.example.capstonedesign1.domain.auth.jwt.JwtProvider;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.example.capstonedesign1.global.dto.ErrorResponseDto;
import org.example.capstonedesign1.global.exception.CustomException;
import org.example.capstonedesign1.global.exception.UnAuthenticationException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserQueryService userQueryService;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String requestUri =  request.getRequestURI();
//        return (requestUri.startsWith("/auth/sign-up")
//                && !requestUri.startsWith("/auth/sign-up/complete"))
//                || requestUri.startsWith("/auth/login")
//                || requestUri.startsWith("/swagger-ui")
//                || requestUri.startsWith("/v3/api-docs");

        String requestUri =  request.getRequestURI();
        boolean skip = (requestUri.startsWith("/auth/sign-up")
                && !requestUri.startsWith("/auth/sign-up/complete"))
                || requestUri.startsWith("/auth/login")
                || requestUri.startsWith("/swagger-ui")
                || requestUri.startsWith("/v3/api-docs");

        log.info("{}, {}", requestUri, String.valueOf(skip));
        return skip;
    }

    /**
     * entryPoint를 사용하면 응답 통일 시 사용하던 errorCode의 customStatus나 customMessage를 사용할 수 없으므로 필터에서 예외를 직접 처리
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            UUID userId = UUID.fromString(parsePayload(request, response));
            User user = userQueryService.findById(userId);
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    List.of(new SimpleGrantedAuthority(user.getRole().toString()))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException expiredJwtException){
            sendErrorResponse(response, new UnAuthenticationException(ErrorCode.EXPIRED_JWT_TOKEN));
        }catch (Exception e){
            sendErrorResponse(response, new UnAuthenticationException(ErrorCode.INVALID_JWT_TOKEN));
        }
    }

    /**
     * @return jwt 토큰에서 파싱한 payload 반환
     */
    private String parsePayload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bearerToken = request.getHeader("Authorization");
        String tokenValue = BearerEncoder.decode(bearerToken);
        return jwtProvider.parsePayload(tokenValue);
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   CustomException customException) throws IOException {
        HttpStatus httpStatus = HttpStatus.valueOf(customException.getErrorCode().getStatus().substring(0,3));

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(
                ErrorResponseDto.res(customException)));
    }
}
