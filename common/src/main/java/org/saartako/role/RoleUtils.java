package org.saartako.role;

public class RoleUtils {

    private RoleUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static String toString(Role role) {
        return "Role{" +
               "id=" + role.getId() +
               ", type='" + role.getType() + '\'' +
               '}';
    }
}
