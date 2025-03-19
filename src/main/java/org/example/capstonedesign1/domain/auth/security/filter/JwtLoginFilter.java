package org.example.capstonedesign1.domain.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.auth.dto.request.LoginRequest;
import org.example.capstonedesign1.domain.auth.dto.response.AuthenticationResponse;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.auth.service.AuthService;
import org.example.capstonedesign1.domain.auth.service.TokenService;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserService;
import org.example.capstonedesign1.global.dto.ErrorResponseDto;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.example.capstonedesign1.global.exception.BadRequestException;
import org.example.capstonedesign1.global.exception.UnAuthenticationException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        LoginRequest loginRequest = parseLoginRequest(request);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        User user = customUserDetails.getUser();

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                tokenService.issueAccessToken(user.getId().toString()), user.isRegisterCompleted());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                ResponseDto.res(HttpStatus.OK, "로그인 성공", authenticationResponse)));
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                           AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String jsonErrorResponse = objectMapper.writeValueAsString(
                ErrorResponseDto.res(new UnAuthenticationException(ErrorCode.USER_UNAUTHORIZED)));
        response.getWriter().write(jsonErrorResponse);
    }


    private LoginRequest parseLoginRequest(HttpServletRequest request){
        try {
            return objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            log.error("errorMessage: {}", e.getMessage());
            throw new BadRequestException(ErrorCode.JSON_PARSE_ERROR, e.getMessage());
        }
    }

}
