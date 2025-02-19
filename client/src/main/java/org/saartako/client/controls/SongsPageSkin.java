package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.constants.Route;
import org.saartako.client.events.ListItemClickEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.song.Song;

import java.util.List;

public class SongsPageSkin extends SkinBase<SongsPage> {

    private final SongService songService = SongService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final CustomTextField searchTextField = new CustomTextField();

    private final MusicCardGrid musicCardGrid = new MusicCardGrid();

    private final ScrollPane contentPane = new ScrollPane(this.musicCardGrid);

    private final Loader loader = new Loader();

    private final VBox node = new VBox(16);

    public SongsPageSkin(SongsPage control) {
        super(control);

        this.node.setAlignment(Pos.TOP_CENTER);
        this.node.setPadding(new Insets(8, 40, 8, 40));

        this.searchTextField.setPromptText("Search");
        this.searchTextField.setMaxWidth(300);
        this.searchTextField.setLeft(new FontIcon(Material2MZ.SEARCH));
        this.searchTextField.setRight(new FontIcon(Material2AL.CLEAR));
        this.node.getChildren().add(this.searchTextField);

        this.node.getChildren().add(this.loader);

        this.contentPane.setFitToWidth(true);

        VBox.setVgrow(this.loader, Priority.ALWAYS);
        VBox.setVgrow(this.contentPane, Priority.ALWAYS);

        this.musicCardGrid.addEventHandler(ListItemClickEvent.LIST_ITEM_CLICK, event -> {
            final int index = event.getIndex();
            final Song song = this.songService.getSongs().get(index);

            this.songService.setCurrentSong(song);
            this.routerService.setCurrentRoute(Route.SONG_VIEW);
        });

        registerChangeListener(this.searchTextField.textProperty(), observable -> updateSongs());
        registerListChangeListener(this.songService.songsProperty(), observable -> updateSongs());
        updateSongs();

        getChildren().setAll(this.node);
    }

    private void updateSongs() {
        updateSongs(this.songService.getSongs(), this.searchTextField.getText());
    }

    private void updateSongs(ObservableList<Song> songs, String search) {
        if (songs == null) {
            this.node.getChildren().set(1, this.loader);
        } else {
            final List<? extends Song> filtered = this.songService.filterSongs(songs, search);

            final List<CardItem> cardItems = filtered.stream().map(SongUtils::songToCardItem).toList();

            Platform.runLater(() -> {
                this.musicCardGrid.cardItemsProperty().setAll(cardItems);
                this.node.getChildren().set(1, this.contentPane);
            });
        }
    }
}
