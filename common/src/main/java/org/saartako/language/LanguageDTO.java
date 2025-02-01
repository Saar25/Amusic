package org.saartako.language;

public class LanguageDTO implements Language {

    private long id;
    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "LanguageDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
