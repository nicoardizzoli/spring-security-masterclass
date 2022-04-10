package com.nicoardizzoli.springsecurityclient.service;

import com.nicoardizzoli.springsecurityclient.dto.UserDto;
import com.nicoardizzoli.springsecurityclient.entity.User;
import com.nicoardizzoli.springsecurityclient.entity.VerificationToken;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    User registerUser(UserDto userDto, HttpServletRequest request);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken, HttpServletRequest request);

    User findUserByEmail(String email);

    String resetPassword(String email);
}
