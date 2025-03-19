package org.example.capstonedesign1.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.jwt.JwtProvider;
import org.example.capstonedesign1.domain.auth.security.CustomAuthenticationEntryPoint;
import org.example.capstonedesign1.domain.auth.security.filter.JwtAuthenticationFilter;
import org.example.capstonedesign1.domain.auth.security.filter.JwtLoginFilter;
import org.example.capstonedesign1.domain.auth.service.TokenCommandService;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenCommandService tokenCommandService;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final UserQueryService userQueryService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/auth/sign-up", "/auth/login").permitAll()
                        .anyRequest().authenticated())
                ;

        http
                .addFilterAt(getJwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(customAuthenticationEntryPoint()))
                ;


        return http.build();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtProvider, userQueryService, objectMapper);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private JwtLoginFilter getJwtLoginFilter() throws Exception {

        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(
                authenticationManager(authenticationConfiguration),
                tokenCommandService,
                objectMapper);
        jwtLoginFilter.setFilterProcessesUrl("/auth/login");
        return jwtLoginFilter;
    }
}
