package com.jwtsecurity.jwtsecurity.Controller;


import com.jwtsecurity.jwtsecurity.Entites.User;
import com.jwtsecurity.jwtsecurity.Exceptions.UserNameNotFoundException;
import com.jwtsecurity.jwtsecurity.LoginDto.LoginDto;
import com.jwtsecurity.jwtsecurity.Services.AuthService.IAuthService;
import com.jwtsecurity.jwtsecurity.Services.User.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {

     private final IAuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<?> getUser(@RequestBody @Valid User user){
        System.out.println("username" + user.getName());
        System.out.println("email" + user.getEmail());
        System.out.println("password" + user.getPassword());

          try{
              if(user.getName() == null || user.getEmail() == null || user.getPassword() == null){
                  throw new IllegalArgumentException("All filed are required !!");
              }
              User newUser = authService.CreateUser(user);
              return ResponseEntity.status(HttpStatus.CREATED)
                      .body(Map.of("message", "User registered successfully"));
          } catch (IllegalArgumentException e) {
              return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",e.getMessage()));
          } catch (RuntimeException e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(Map.of("error",e.getMessage()));
          }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto user, HttpServletResponse response){
           try{
               if(user.getEmail() == null || user.getPassword() == null){
                   throw new IllegalArgumentException("All filed are required !!");
               }

            var token = authService.verifyUser(user);
               Cookie jwtToken = new Cookie("token", token);
               jwtToken.setHttpOnly(true);
               jwtToken.setSecure(true);
               jwtToken.setPath("/");
               jwtToken.setMaxAge(10*60);
               response.addCookie(jwtToken);

            return  ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message","User login successfully",
                       "token", token
            ));
           } catch (UserNameNotFoundException e) {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body(Map.of("error",e.getMessage()));
           } catch (RuntimeException e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(e.getMessage());
           }
    }


}
