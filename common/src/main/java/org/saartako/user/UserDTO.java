package org.saartako.user;

public class UserDTO implements User {

    private long id;
    private String username;
    private String password;
    private String displayName;

    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

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
