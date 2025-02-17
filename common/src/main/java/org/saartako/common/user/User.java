package org.saartako.common.user;

import org.saartako.common.role.Role;

import java.util.Collection;

public interface User {
    long getId();

    String getUsername();

    String getPassword();

    String getSalt();

    String getDisplayName();

    Collection<? extends Role> getRoles();
}
