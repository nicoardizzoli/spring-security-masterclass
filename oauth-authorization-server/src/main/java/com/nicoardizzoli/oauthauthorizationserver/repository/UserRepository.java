package com.nicoardizzoli.oauthauthorizationserver.repository;

import com.nicoardizzoli.oauthauthorizationserver.entity.UserLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserLocal, Long> {

    Optional<UserLocal> findUserByEmail(String email);
}
