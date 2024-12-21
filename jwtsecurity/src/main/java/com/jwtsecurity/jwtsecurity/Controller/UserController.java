package com.jwtsecurity.jwtsecurity.Controller;

import com.jwtsecurity.jwtsecurity.Entites.User;
import com.jwtsecurity.jwtsecurity.Exceptions.ResourceNotFoundException;
import com.jwtsecurity.jwtsecurity.Services.User.IUserService;
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
public class UserController {

      private final IUserService userService;


      @GetMapping("/findByUserId/{userId}")
      public ResponseEntity<?> getUserById(@PathVariable UUID userId){
            try{
                User  user = userService.findByUserId(userId);
                return ResponseEntity.status(HttpStatus.FOUND).body(Map.of("user",user));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",e.getMessage()));
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
      }

      @GetMapping("/findByUserName/{name}")
      public ResponseEntity<?> getUserByName(@PathVariable String name){
          try{
              User  user = userService.findUserByName(name);
              return ResponseEntity.status(HttpStatus.FOUND).body(Map.of("user",user));
          } catch (ResourceNotFoundException e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",e.getMessage()));
          } catch (RuntimeException e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
          }
      }

    @GetMapping("/findByUserEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        try{
            User  user = userService.findUserByEmail(email);
            return ResponseEntity.status(HttpStatus.FOUND).body(Map.of("user",user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
