package org.saartako.language;

public class LanguageDTO implements Language {

    private long id;
    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    public LanguageDTO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public LanguageDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return LanguageUtils.toString(this);
    }
}
