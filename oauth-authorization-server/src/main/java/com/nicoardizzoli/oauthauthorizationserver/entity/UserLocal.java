package com.nicoardizzoli.oauthauthorizationserver.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLocal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    //vamos a usar bcript encoder
    @Column(length = 60)
    private String password;
    private String role;

    @Builder.Default
    private boolean enabled = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserLocal userLocal = (UserLocal) o;
        return id != null && Objects.equals(id, userLocal.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
