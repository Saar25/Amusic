package org.saartako.client.constants;

public enum Route {
    LOGIN("login"),
    SONGS("tmp"),
    TEST("test"),
    ;

    private final String name;

    Route(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
