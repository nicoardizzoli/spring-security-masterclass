package com.nicoardizzoli.springsecurityclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {
    
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/register",
            "/api/v1/verifyRegistration",
            "/api/v1/resendVerificationToken"
    };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers(WHITE_LIST_URL)
                .permitAll()
                .antMatchers("/api/v1/**").authenticated()
                .and()
                .oauth2Login(oa -> oa.loginPage("/oauth2/authorization/api-client-oidc"))
                .oauth2Client(Customizer.withDefaults());

        return httpSecurity.build();
    }



}
