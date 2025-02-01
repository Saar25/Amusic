package org.saartako.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT p FROM users p WHERE p.username = :username AND p.password = :password")
    Optional<UserEntity> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}