package org.saartako.user;

import org.saartako.role.RoleDTO;

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

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getSalt() {
        return salt;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Set<RoleDTO> getRoles() {
        return this.roles;
    }

    @Override
    public String toString() {
        return UserUtils.toString(this);
    }
}
