package com.nicoardizzoli.springsecurityclient.event;

import com.nicoardizzoli.springsecurityclient.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NewVerificationTokenEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;
    private String token;

    public NewVerificationTokenEvent(User user, String applicationUrl, String token) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
        this.token = token;
    }
}
