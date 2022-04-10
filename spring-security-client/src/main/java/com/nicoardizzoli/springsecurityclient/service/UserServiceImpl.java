package com.nicoardizzoli.springsecurityclient.service;

import com.nicoardizzoli.springsecurityclient.dto.UserDto;
import com.nicoardizzoli.springsecurityclient.entity.PasswordResetToken;
import com.nicoardizzoli.springsecurityclient.entity.User;
import com.nicoardizzoli.springsecurityclient.entity.VerificationToken;
import com.nicoardizzoli.springsecurityclient.event.NewVerificationTokenEvent;
import com.nicoardizzoli.springsecurityclient.event.RegistrationCompleteEvent;
import com.nicoardizzoli.springsecurityclient.repository.PasswordResetTokenRepository;
import com.nicoardizzoli.springsecurityclient.repository.UserRepository;
import com.nicoardizzoli.springsecurityclient.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    @Transactional
    public User registerUser(UserDto userDto, HttpServletRequest request) {
        User userToRegister = User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .role("USER")
                .password(passwordEncoder.encode(userDto.getPassword()))  //OJO QUE PARA GUARDARLA PRIMERO HAY QUE ENCRIPTARLA CON BCRIPT
                .build();

        User user = userRepository.save(userToRegister);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return user;

    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.isEmpty()) {
            return "invalid";
        }

        boolean verificationTimeExpired = verificationToken.filter(vt -> vt.getExpirationTime().isBefore(LocalDateTime.now())).isPresent();
        if (verificationTimeExpired) {
            verificationTokenRepository.delete(verificationToken.get());
            return "expired";
        }

        User user = verificationToken.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";

    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken).orElseThrow(() -> new IllegalArgumentException("Token not found"));
        verificationToken.updateToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        User user = verificationToken.getUser();
        String applicationUrl = applicationUrl(request);
        String token = verificationToken.getToken();

        applicationEventPublisher.publishEvent(new NewVerificationTokenEvent(user, applicationUrl, token));

        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public String resetPassword(String email) {
        try {
            User userByEmail = this.findUserByEmail(email);
            String newToken = UUID.randomUUID().toString();
            this.generateNewPassword(userByEmail, newToken);
        } catch (IllegalArgumentException e) {
            log.info("USER NOT FOUND PROBANDO CATCH");
        }

        return null;
    }

    private void generateNewPassword(User userByEmail, String newToken) {
        passwordResetTokenRepository.save(new PasswordResetToken(newToken, userByEmail));

    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
