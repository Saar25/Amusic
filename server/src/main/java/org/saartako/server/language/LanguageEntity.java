package org.saartako.server.language;

import jakarta.persistence.*;
import org.saartako.common.language.Language;
import org.saartako.common.language.LanguageUtils;

@Entity(name = "languages")
public class LanguageEntity implements Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16)
    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return LanguageUtils.toString(this);
    }
}
