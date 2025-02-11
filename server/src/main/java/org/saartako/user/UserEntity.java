package org.saartako.user;

import jakarta.persistence.*;
import org.saartako.role.RoleEntity;

import java.util.Set;

@Entity(name = "users")
public class UserEntity implements User {

    public static final int USERNAME_LENGTH = 16;
    public static final int DISPLAY_NAME_LENGTH = 16;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = UserEntity.USERNAME_LENGTH, unique = true, nullable = false)
    private String username;

    @Column(length = 44, nullable = false)
    private String password;

    @Column(length = 24, nullable = false)
    private String salt;

    @Column(length = DISPLAY_NAME_LENGTH)
    private String displayName;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Set<RoleEntity> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return UserUtils.toString(this);
    }
}
