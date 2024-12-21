package com.jwtsecurity.jwtsecurity.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
public class JwtService {


    private String SECERT_KEY = "your256bitsecretkeymustbe32characterslong";


    public String generateToken(String email){
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims,email);
    };

    private SecretKey getSigningKey(){
         return Keys.hmacShaKeyFor(SECERT_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(HashMap<String,Object> claims, String subject){

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractedClaim(String token){
           if(token == null || token.isEmpty()){
                throw new IllegalArgumentException("Token cannot be null or empty");
           }
           try{
                  return  Jwts.parser()
                          .verifyWith(getSigningKey())
                          .build()
                          .parseSignedClaims(token)
                          .getPayload();
           }catch (SecurityException | MalformedJwtException e){
               System.out.println("Invalid JWT format or signature: " + e.getMessage());
               throw new IllegalArgumentException("Invalid token format or signature");
           }catch (Exception e){
               System.out.println("Other Jwt parsing error" + e.getMessage());
               throw new IllegalArgumentException("Unable to parse JWT");
           }
    }

    public Boolean validateToken(String token, String email, Claims claims ){
                     try{
                         String tokenEmail = claims.getSubject();
                         return  (email.equals(tokenEmail) && !isTokenExpired(claims));
                     }catch (Exception e){
                          return  false;
                     }
    }

    private Boolean isTokenExpired(Claims claims){
            // Use the 'claims' parameter directly to extract the expiration date
         return  claims.getExpiration().before(new Date());
    }

}
