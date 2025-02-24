package org.saartako.common.role;

public class RoleDTO implements Role {

    private long id;
    private String type;

    @Override
    public long getId() {
        return this.id;
    }

    public RoleDTO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getType() {
        return this.type;
    }

    public RoleDTO setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return RoleUtils.toString(this);
    }
}
