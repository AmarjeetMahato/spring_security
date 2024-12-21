package com.jwtsecurity.jwtsecurity.Services.AuthService;

import com.jwtsecurity.jwtsecurity.Entites.User;
import com.jwtsecurity.jwtsecurity.Exceptions.ResourceNotFoundException;
import com.jwtsecurity.jwtsecurity.Exceptions.UserNameNotFoundException;
import com.jwtsecurity.jwtsecurity.JwtService.JwtService;
import com.jwtsecurity.jwtsecurity.LoginDto.LoginDto;
import com.jwtsecurity.jwtsecurity.Repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Override
    public User CreateUser(User user) {
        return CreateNewUser(user);
    }


    //    Create User
    private User CreateNewUser(User user){
        String getUserEmail = user.getEmail().trim().toLowerCase();

        Optional<User> existUser = authRepository.findByEmail(getUserEmail);

        if(existUser.isPresent()){
            throw  new IllegalArgumentException("User already exist with this email " + getUserEmail);
        }
        user.setEmail(getUserEmail);
        user.setPassword(encoder.encode(user.getPassword()));
        return authRepository.save(user);
    }


    @Override
    public void deleteUser(UUID userId) {
        authRepository.findById(userId).ifPresentOrElse(
                user -> authRepository.delete(user), () -> {
                    throw new ResourceNotFoundException("User not found with ID: " + userId);
                }
        );
    }

    @Override
    public String verifyUser(LoginDto  loginDto) {
           User user = authRepository.findByEmail(loginDto.getEmail().trim().toLowerCase()).orElseThrow(
                   ()->new UserNameNotFoundException("User not found with Email : " + loginDto.getEmail())
           );

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                       loginDto.getEmail(), loginDto.getPassword()
                ));
        if(auth.isAuthenticated()){
              return  jwtService.generateToken(loginDto.getEmail());
        }else{
            return "Failure";
        }

    }

    @Override
    public void logOut(String token) {

    }
}
