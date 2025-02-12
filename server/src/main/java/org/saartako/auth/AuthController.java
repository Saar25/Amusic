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
    public String login(
        @RequestParam("username") String username,
        @RequestParam("password") String password
    ) {
        final Optional<UserEntity> login = authService.login(username, password);

        return login.map(userEntity -> this.authService.createJwt(userEntity)).orElse(null);

    }

    @GetMapping("/register")
    public String register(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("displayName") String displayName
    ) {
        final UserEntity save = authService.save(username, password, displayName);

        return this.authService.createJwt(save);
    }
}
