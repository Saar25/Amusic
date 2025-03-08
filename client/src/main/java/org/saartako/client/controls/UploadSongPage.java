package org.saartako.client.controls;

import javafx.beans.property.ListProperty;
import javafx.scene.control.Control;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.GenreService;
import org.saartako.client.services.LanguageService;
import org.saartako.common.genre.Genre;
import org.saartako.common.language.Language;

public class UploadSongPage extends Control implements RouteNode {

    private final LanguageService languageService = LanguageService.getInstance();
    private final GenreService genreService = GenreService.getInstance();

    @Override
    protected UploadSongPageSkin createDefaultSkin() {
        return new UploadSongPageSkin(this);
    }

    @Override
    public void onEnterView() {
        this.languageService.fetchData();
        this.genreService.fetchData();
    }

    public ListProperty<Language> languagesProperty() {
        return this.languageService.languagesProperty();
    }

    public ListProperty<Genre> genresProperty() {
        return this.genreService.genresProperty();
    }
}
