package com.nicoardizzoli.oauthauthorizationserver.service;

import com.nicoardizzoli.oauthauthorizationserver.entity.UserLocal;
import com.nicoardizzoli.oauthauthorizationserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserLocal userLocal = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return User.builder()
                .username(userLocal.getEmail())
                .password(userLocal.getPassword())
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(!userLocal.isEnabled())
                .authorities(getAuthorities(List.of(userLocal.getRole())))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return authorities;
    }
}
