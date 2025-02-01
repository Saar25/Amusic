package org.saartako.auth;

import org.saartako.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public Optional<UserEntity> login(
        @RequestParam("username") String username,
        @RequestParam("password") String password
    ) {
        return authService.login(username, password);
    }

    @GetMapping("/register")
    public UserEntity registry(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("displayName") String displayName
    ) {
        return authService.save(username, password, displayName);
    }
}
