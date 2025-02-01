package org.saartako.auth;

import jakarta.persistence.EntityNotFoundException;
import org.saartako.encrypt.Encryption;
import org.saartako.encrypt.Encryptions;
import org.saartako.user.UserEntity;
import org.saartako.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> login(String username, String password) {
        final Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername(username);
        if (optionalUserEntity.isEmpty()) {
            throw new EntityNotFoundException();
        }
        final UserEntity userEntity = optionalUserEntity.get();

        final Encryption encryption = Encryptions.getDefaultEncryption();
        final String encrypt = encryption.encrypt(password, userEntity.getSalt());
        if (!encrypt.equals(userEntity.getPassword())) {
            throw new EntityNotFoundException();
        }

        return optionalUserEntity;
    }

    public UserEntity save(String username, String password, String displayName) {
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