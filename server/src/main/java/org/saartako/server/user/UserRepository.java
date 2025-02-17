package org.saartako.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT p FROM users p WHERE p.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query("SELECT p FROM users p LEFT JOIN FETCH p.roles WHERE p.username = :username ")
    Optional<UserEntity> findByUsernameWithRoles(@Param("username") String username);

}