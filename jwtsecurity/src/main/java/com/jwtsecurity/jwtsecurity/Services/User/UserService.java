package com.jwtsecurity.jwtsecurity.Services.User;

import com.jwtsecurity.jwtsecurity.Entites.User;
import com.jwtsecurity.jwtsecurity.Exceptions.ResourceNotFoundException;
import com.jwtsecurity.jwtsecurity.Exceptions.UserNameNotFoundException;
import com.jwtsecurity.jwtsecurity.Repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class UserService  implements IUserService{

    @Autowired
    private AuthRepository authRepository;


    @Override
    public User findByUserId(UUID userId) {
          return authRepository.findById(userId)
                  .orElseThrow(() -> new UserNameNotFoundException("User not found with ID: " + userId));

    }

    @Override
    public User findUserByName(String name) {
        return authRepository.findByName(name)
                .orElseThrow(()-> new UsernameNotFoundException("user not found with name " + name));
    }

    @Override
    public User findUserByEmail(String email) {
        return authRepository.findByEmail(email)
                .orElseThrow(()-> new UserNameNotFoundException("User not found with email " + email));
    }


    @Override
    public List<User> getAllUser() {
        return authRepository.findAll();
    }
}
