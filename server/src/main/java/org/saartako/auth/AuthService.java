package org.saartako.auth;

import org.saartako.encrypt.Encryption;
import org.saartako.encrypt.Encryptions;
import org.saartako.exceptions.BadCredentialsException;
import org.saartako.exceptions.UserAlreadyExistsException;
import org.saartako.exceptions.UserNotFoundException;
import org.saartako.user.UserEntity;
import org.saartako.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> login(String username, String password) {
        final Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername(username);
        if (optionalUserEntity.isEmpty()) {
            throw new UserNotFoundException("Username not found");
        }
        final UserEntity userEntity = optionalUserEntity.get();

        final Encryption encryption = Encryptions.getDefaultEncryption();
        final String encrypt = encryption.encrypt(password, userEntity.getSalt());
        if (!encrypt.equals(userEntity.getPassword())) {
            throw new BadCredentialsException("Password incorrect");
        }

        return optionalUserEntity;
    }

    public UserEntity save(String username, String password, String displayName) {
        final Optional<UserEntity> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
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
        return this.userRepository.save(userEntity);
    }
}
