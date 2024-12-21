package com.jwtsecurity.jwtsecurity.Services.AuthService;


import com.jwtsecurity.jwtsecurity.Entites.User;
import com.jwtsecurity.jwtsecurity.LoginDto.LoginDto;

import java.util.UUID;

public interface IAuthService {

    User CreateUser(User user);
    void deleteUser(UUID userId);
    String verifyUser(LoginDto user);
    void logOut(String token);
}
