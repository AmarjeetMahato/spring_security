package com.jwtsecurity.jwtsecurity.CustomUserDetailsService;


import com.jwtsecurity.jwtsecurity.CustomUserDetails.CustomUserDetails;
import com.jwtsecurity.jwtsecurity.Entites.User;
import com.jwtsecurity.jwtsecurity.Exceptions.UserNameNotFoundException;
import com.jwtsecurity.jwtsecurity.Repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
             User user = authRepository.findByEmail(email)
                     .orElseThrow(()-> new UserNameNotFoundException("User not found with email " + email ));
      return  new CustomUserDetails(user);
    }
}
