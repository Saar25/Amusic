package org.saartako.user;

import org.saartako.roles.Role;

import java.util.Collection;

public interface User {
    long getId();

    String getUsername();

    String getPassword();

    String getSalt();

    String getDisplayName();

    Collection<? extends Role> getRoles();
}
