package com.jwtsecurity.jwtsecurity.Repository;

import com.jwtsecurity.jwtsecurity.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository  extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
}
