package org.saartako.common.user;

import org.saartako.common.role.RoleDTO;

import java.util.Set;

public class UserDTO implements User {

    private long id;
    private String username;
    private String password;
    private String salt;
    private String displayName;
    private Set<RoleDTO> roles;

    @Override
    public long getId() {
        return this.id;
    }

    public UserDTO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getSalt() {
        return salt;
    }

    public UserDTO setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public UserDTO setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public Set<RoleDTO> getRoles() {
        return this.roles;
    }

    public UserDTO setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public String toString() {
        return UserUtils.toString(this);
    }
}
