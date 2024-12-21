package com.jwtsecurity.jwtsecurity.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class HelloUser {

    @GetMapping("/user")
    public String user(){
        System.out.println("Hello world");
        return  "hello World";
    }
}
