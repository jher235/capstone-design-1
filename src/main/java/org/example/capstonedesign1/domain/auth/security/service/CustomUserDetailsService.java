package org.example.capstonedesign1.domain.auth.security.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.repository.UserRepository;
import org.example.capstonedesign1.domain.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);
        // 추후 OAuth 추가 시 인증 로직 추가.
        return new CustomUserDetails(user);
    }
}
