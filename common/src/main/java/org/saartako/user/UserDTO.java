package org.saartako.user;

public class UserDTO implements User {

    private long id;
    private String username;
    private String password;
    private String salt;
    private String displayName;

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
    public String toString() {
        return "UserDTO{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", displayName='" + displayName + '\'' +
               '}';
    }
}
