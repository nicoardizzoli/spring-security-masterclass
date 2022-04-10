package com.nicoardizzoli.springsecurityclient.event.listener;

import com.nicoardizzoli.springsecurityclient.entity.User;
import com.nicoardizzoli.springsecurityclient.event.NewVerificationTokenEvent;
import com.nicoardizzoli.springsecurityclient.event.RegistrationCompleteEvent;
import com.nicoardizzoli.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class NewVerificationTokenEventListener implements ApplicationListener<NewVerificationTokenEvent> {


    @Override
    public void onApplicationEvent(NewVerificationTokenEvent event) {

        //TODO send mail to user (por ahora lo hacemos en consola)
        String url = event.getApplicationUrl() + "/api/v1/verifyRegistration?token=" + event.getToken();
        log.info("Click the link to verify your account: {}", url);
    }
}
