package org.saartako.server.role;

import jakarta.persistence.*;
import org.saartako.common.role.Role;
import org.saartako.common.role.RoleUtils;

@Entity(name = "roles")
public class RoleEntity implements Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16)
    private String type;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return RoleUtils.toString(this);
    }
}
