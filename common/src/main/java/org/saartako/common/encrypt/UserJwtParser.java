package org.saartako.common.encrypt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import org.saartako.common.user.User;
import org.saartako.common.user.UserDTO;
import org.saartako.common.user.UserUtils;

import java.util.Base64;

public class UserJwtParser implements JwtParser<User> {

    private static final Gson GSON = new Gson();

    @Override
    public String sign(Algorithm algorithm, User user) {
        final UserDTO userDTO = UserUtils.copySafe(user);

        final String payload = GSON.toJson(userDTO, UserDTO.class);

        return JWT.create().withPayload(payload).sign(algorithm);
    }

    @Override
    public User verify(Algorithm algorithm, String token) {
        final DecodedJWT verify = JWT.require(algorithm).build().verify(token);

        final String base64 = verify.getPayload();

        return base64ToUser(base64);
    }

    @Override
    public User parse(String token) {
        final DecodedJWT verify = JWT.decode(token);

        final String base64 = verify.getPayload();

        return base64ToUser(base64);
    }

    private UserDTO base64ToUser(String base64) {
        final String payload = new String(Base64.getDecoder().decode(base64));

        return GSON.fromJson(payload, UserDTO.class);
    }
}