package com.nicoardizzoli.springsecurityclient.controller;

import com.nicoardizzoli.springsecurityclient.dto.PasswordDto;
import com.nicoardizzoli.springsecurityclient.dto.UserDto;
import com.nicoardizzoli.springsecurityclient.entity.User;
import com.nicoardizzoli.springsecurityclient.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String registerUser(@RequestBody UserDto userDto, final HttpServletRequest request) {
        User user = userService.registerUser(userDto, request);
        return "User registration successfully: " + user.getFirstName();
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        if (userService.validateVerificationToken(token).equals("valid")) {
            return "User Verifies Successfully";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        userService.generateNewVerificationToken(oldToken, request);
        return "new token generated";
    }

//    @PostMapping("/resetPassword")
//    public String resetPassword(@RequestBody PasswordDto passwordDto) {
//        userService.findUserByEmail(passwordDto.getEmail());
//
//    }

}
