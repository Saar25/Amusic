package org.saartako.common.language;

public class LanguageUtils {

    private LanguageUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static LanguageDTO copy(Language language) {
        return language == null
            ? null
            : new LanguageDTO()
                .setId(language.getId())
                .setName(language.getName());
    }

    public static String toString(Language language) {
        return "Language{" +
               "id=" + language.getId() +
               ", name='" + language.getName() + '\'' +
               '}';
    }
}
