package org.saartako.server.auth;

import org.saartako.common.encrypt.Encryption;
import org.saartako.common.encrypt.Encryptions;
import org.saartako.common.user.User;
import org.saartako.server.user.UserEntity;
import org.saartako.server.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public Optional<UserEntity> login(String username, String password) {
        if (username.length() > UserEntity.USERNAME_LENGTH) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No such user");
        }

        final Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsernameWithRoles(username);
        if (optionalUserEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No such user");
        }

        final UserEntity userEntity = optionalUserEntity.get();

        final Encryption encryption = Encryptions.getDefaultEncryption();
        final String encrypt = encryption.encrypt(password, userEntity.getSalt());
        if (!encrypt.equals(userEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }

        return optionalUserEntity;
    }

    public UserEntity register(String username, String password, String displayName) {
        if (username.length() > UserEntity.USERNAME_LENGTH ||
            displayName.length() > UserEntity.DISPLAY_NAME_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Username and display name must be of length less than 16 chars");
        }

        final Optional<UserEntity> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User already exists");
        }

        final byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);
        final String salt = Base64.getEncoder().encodeToString(saltBytes);

        final Encryption encryption = Encryptions.getDefaultEncryption();
        final String encryptedPassword = encryption.encrypt(password, salt);

        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(encryptedPassword);
        userEntity.setSalt(salt);
        userEntity.setDisplayName(displayName);
        userEntity.setRoles(Set.of());
        return this.userRepository.save(userEntity);
    }

    public String createJwt(User user) {
        return this.jwtService.sign(user);
    }
}
