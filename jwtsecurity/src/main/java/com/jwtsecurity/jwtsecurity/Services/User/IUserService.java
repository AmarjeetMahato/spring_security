package com.jwtsecurity.jwtsecurity.Services.User;

import com.jwtsecurity.jwtsecurity.Entites.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {


    User findByUserId(UUID userId);
    User findUserByName(String name);
    User findUserByEmail(String email);

    List<User> getAllUser();

}
