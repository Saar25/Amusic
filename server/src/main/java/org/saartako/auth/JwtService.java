package org.saartako.auth;

import com.auth0.jwt.algorithms.Algorithm;
import org.saartako.encrypt.JwtParser;
import org.saartako.encrypt.UserJwtParser;
import org.saartako.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtParser<User> userJwtParser = new UserJwtParser();

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String sign(User user) {
        final Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);

        return this.userJwtParser.sign(algorithm, user);
    }

    public User verify(String token) {
        final Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);

        return this.userJwtParser.verify(algorithm, token);
    }
}
