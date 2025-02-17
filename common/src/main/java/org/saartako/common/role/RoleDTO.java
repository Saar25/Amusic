package org.saartako.common.role;

public class RoleDTO implements Role {

    private long id;
    private String type;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return RoleUtils.toString(this);
    }
}
