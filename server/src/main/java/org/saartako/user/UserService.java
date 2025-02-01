package org.saartako.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> findAll() {
        return this.userRepository.findAll();
    }

    public Optional<UserEntity> findById(long id) {
        return this.userRepository.findById(id);
    }

    public Optional<UserEntity> findByUsernameAndPassword(String username, String password) {
        return this.userRepository.findByUsernameAndPassword(username, password);
    }

    public UserEntity save(String username, String password, String displayName) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setDisplayName(displayName);
        return this.userRepository.save(userEntity);
    }
}