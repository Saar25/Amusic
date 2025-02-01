package org.saartako.user;

public class UserUtils {

    private UserUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static String toString(User user) {
        return "User{" +
               "id=" + user.getId() +
               ", username='" + user.getUsername() + '\'' +
               ", password='" + user.getPassword() + '\'' +
               ", salt='" + user.getSalt() + '\'' +
               ", displayName='" + user.getDisplayName() + '\'' +
               ", roles=" + user.getRoles() +
               '}';
    }
}
