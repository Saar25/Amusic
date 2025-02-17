package org.saartako.server.auth;

import org.saartako.common.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> login(
        @RequestParam("username") String username,
        @RequestParam("password") String password
    ) {
        final Optional<? extends User> login = this.authService.login(username, password);

        if (login.isPresent()) {
            final User user = login.get();
            final String jwt = this.authService.createJwt(user);
            return ResponseEntity.status(HttpStatus.OK).body(jwt);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/register")
    public ResponseEntity<String> register(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("displayName") String displayName
    ) {
        final User user = this.authService.save(username, password, displayName);
        final String jwt = this.authService.createJwt(user);
        return ResponseEntity.accepted().body(jwt);
    }
}
