package org.saartako.common.role;

import java.util.Collection;
import java.util.Objects;

public class RoleUtils {

    private RoleUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static boolean hasRoleOfType(Collection<? extends Role> roles, String type) {
        return roles.stream().anyMatch(role -> Objects.equals(role.getType(), type));
    }

    public static RoleDTO copy(Role role) {
        return role == null
            ? null
            : new RoleDTO()
                .setId(role.getId())
                .setType(role.getType());
    }

    public static String toString(Role role) {
        return "Role{" +
               "id=" + role.getId() +
               ", type='" + role.getType() + '\'' +
               '}';
    }
}
