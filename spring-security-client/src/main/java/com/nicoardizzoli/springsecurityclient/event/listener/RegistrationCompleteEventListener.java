package com.nicoardizzoli.springsecurityclient.event.listener;

import com.nicoardizzoli.springsecurityclient.entity.User;
import com.nicoardizzoli.springsecurityclient.event.RegistrationCompleteEvent;
import com.nicoardizzoli.springsecurityclient.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //TODO create the verification token for the user with Link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);

        //TODO send mail to user (por ahora lo hacemos en consola)
        String url = event.getApplicationUrl() + "/api/v1/verifyRegistration?token=" + token;
        log.info("Click the link to verify your account: {}", url);
    }
}
