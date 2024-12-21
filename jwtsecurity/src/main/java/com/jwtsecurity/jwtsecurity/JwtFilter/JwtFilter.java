package com.jwtsecurity.jwtsecurity.JwtFilter;


import com.jwtsecurity.jwtsecurity.CustomUserDetailsService.CustomUserDetailsService;
import com.jwtsecurity.jwtsecurity.JwtService.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

          String authorizationHeader = request.getHeader("Authorization");  // Retrieve the Authorization header

         String email = null;
         String jwt = null;
        Claims claims = null;

        // Check if the Authorization header contains a Bearer token
         if(authorizationHeader != null &&  authorizationHeader.startsWith("Bearer")){
               jwt =  authorizationHeader.substring(7);
             System.out.println("Token after parsing: " + jwt); // Log for debugging

               try{
                      claims = jwtService.extractedClaim(jwt);
                      email = claims.getSubject();
               }catch (Exception e){
                     response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                   response.getWriter().write("Invalid JWT token");
                   return;
               }

             // If a valid username is found and not authenticated yet
             if(email != null || SecurityContextHolder.getContext().getAuthentication() == null){
                 UserDetails userDetails = userDetailsService.loadUserByUsername(email); // Load user details
                 if(jwtService.validateToken(jwt,userDetails.getUsername(),claims)){ // Validate the token
                     UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                             userDetails, null, userDetails.getAuthorities()); // Create authentication object
                     auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set details
                     SecurityContextHolder.getContext().setAuthentication(auth); // Set authentication context
                 }

             }
         }
    }
}
