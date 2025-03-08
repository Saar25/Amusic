package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.events.CardItemEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.utils.GridUtils;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.song.Song;

import java.util.List;

public class SongsPageSkin extends SkinBase<SongsPage> {

    private final GridPane songGrid = new GridPane();

    private final ScrollPane contentPane = new ScrollPane(this.songGrid);

    private final Loader loader = new Loader();

    private final VBox node = new VBox(Config.GAP_LARGE);

    public SongsPageSkin(SongsPage control) {
        super(control);

        this.node.setAlignment(Pos.TOP_CENTER);
        this.node.setPadding(new Insets(Config.GAP_MEDIUM, Config.GAP_HUGE, Config.GAP_MEDIUM, Config.GAP_HUGE));

        final CustomTextField searchTextField = new CustomTextField();
        searchTextField.setPromptText("Search");
        searchTextField.setMaxWidth(300);
        searchTextField.setLeft(new FontIcon(Material2MZ.SEARCH));
        searchTextField.setRight(new FontIcon(Material2AL.CLEAR));
        registerChangeListener(searchTextField.textProperty(), observable -> {
            final String filter = searchTextField.textProperty().get();
            getSkinnable().songsFilterProperty().set(filter);
        });
        this.node.getChildren().add(searchTextField);

        this.node.getChildren().add(this.loader);

        this.contentPane.setFitToWidth(true);

        VBox.setVgrow(this.loader, Priority.ALWAYS);
        VBox.setVgrow(this.contentPane, Priority.ALWAYS);

        GridUtils.initializeGrid(this.songGrid,
            Config.GRID_LARGE_COLUMNS, 0, Config.GAP_LARGE, Config.GAP_MEDIUM);

        registerListChangeListener(getSkinnable().filteredSongsProperty(), observable -> updateSongs());
        updateSongs();

        getChildren().setAll(this.node);
    }

    private void updateSongs() {
        updateSongs(getSkinnable().filteredSongsProperty().get());
    }

    private void updateSongs(ObservableList<Song> songs) {
        if (songs == null) {
            this.node.getChildren().set(1, this.loader);
        } else {
            final List<MusicCard> musicCards = songs.stream()
                .map(song -> {
                    final CardItem cardItem = SongUtils.songToCardItem(song);
                    final MusicCard musicCard = new MusicCard();
                    musicCard.setCardItem(cardItem);
                    musicCard.setExpandable(true);
                    musicCard.addEventFilter(CardItemEvent.EXPAND_CARD_ITEM, event -> {
                        getSkinnable().onExpandSong(song);
                    });
                    return musicCard;
                })
                .toList();

            Platform.runLater(() -> {
                this.songGrid.getChildren().clear();
                GridUtils.addInColumns(this.songGrid, musicCards);
                this.node.getChildren().set(1, this.contentPane);
            });
        }
    }
}
