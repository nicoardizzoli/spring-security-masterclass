package com.nicoardizzoli.springsecurityclient.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class PasswordResetToken {

    private static final Long EXPIRATION_TIME = 10L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expirationTime;

    @OneToOne()
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"),
            nullable = false
    )
    private User user;


    public PasswordResetToken(String token) {
        this.token = token;
        this.expirationTime = this.calculateExpirationTime();
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = this.calculateExpirationTime();
    }

    private LocalDateTime calculateExpirationTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(PasswordResetToken.EXPIRATION_TIME);
    }

    public void updateToken(String token){
        this.setToken(token);
        this.setExpirationTime(calculateExpirationTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PasswordResetToken that = (PasswordResetToken) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
