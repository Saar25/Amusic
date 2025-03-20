package org.saartako.common.user;

import org.saartako.common.role.Role;
import org.saartako.common.role.RoleUtils;

import java.util.stream.Collectors;

public class UserUtils {

    private UserUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static boolean isAdmin(User user) {
        return user != null && user.getRoles() != null && RoleUtils.hasRoleOfType(user.getRoles(), Role.ADMIN_TYPE);
    }

    public static UserDTO copyDisplay(User user) {
        return user == null
            ? null
            : new UserDTO()
                .setId(user.getId())
                .setDisplayName(user.getDisplayName());
    }

    public static UserDTO copySafe(User user) {
        return user == null
            ? null
            : new UserDTO()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setDisplayName(user.getDisplayName())
                .setRoles(user.getRoles().stream().map(RoleUtils::copy).collect(Collectors.toSet()));
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
