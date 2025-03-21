package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.components.Loader;
import org.saartako.client.events.CardItemEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.utils.GridUtils;
import org.saartako.client.utils.CardItemUtils;
import org.saartako.common.playlist.Playlist;

import java.util.List;

public class PlaylistsPageSkin extends SkinBase<PlaylistsPage> {

    private final GridPane playlistGrid = new GridPane();

    private final ScrollPane contentPane = new ScrollPane(this.playlistGrid);

    private final Loader loader = new Loader();

    private final Button createPlaylistButton = new Button("Create new playlist");

    private final VBox node = new VBox(Config.GAP_LARGE);

    public PlaylistsPageSkin(PlaylistsPage control) {
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
            getSkinnable().playlistsFilterProperty().set(filter);
        });
        this.node.getChildren().add(searchTextField);

        this.node.getChildren().add(this.loader);

        this.contentPane.setFitToWidth(true);

        VBox.setVgrow(this.loader, Priority.ALWAYS);
        VBox.setVgrow(this.contentPane, Priority.ALWAYS);

        GridUtils.initializeGrid(this.playlistGrid,
            Config.GRID_LARGE_COLUMNS, 0, Config.GAP_LARGE, Config.GAP_MEDIUM);

        this.createPlaylistButton.getStyleClass().add(Styles.ACCENT);
        this.createPlaylistButton.setOnAction(event -> getSkinnable().onCreatePlaylistButtonClick());
        this.node.getChildren().add(this.createPlaylistButton);

        registerListChangeListener(getSkinnable().filteredPlaylistsProperty(), observable -> updatePlaylists());
        updatePlaylists();

        getChildren().setAll(this.node);
    }

    private void updatePlaylists() {
        updatePlaylists(getSkinnable().filteredPlaylistsProperty().get());
    }

    private void updatePlaylists(ObservableList<Playlist> playlists) {
        if (playlists == null) {
            Platform.runLater(() -> {
                this.playlistGrid.getChildren().clear();
                this.node.getChildren().set(1, this.loader);
                this.createPlaylistButton.setVisible(false);
            });
        } else {
            final List<MusicCard> musicCards = playlists.stream()
                .map(playlist -> {
                    final CardItem cardItem = CardItemUtils.playlistToCardItem(playlist);
                    final MusicCard musicCard = new MusicCard();
                    musicCard.setCardItem(cardItem);
                    musicCard.setExpandable(true);
                    musicCard.addEventFilter(CardItemEvent.EXPAND_CARD_ITEM, event -> {
                        getSkinnable().onExpandPlaylist(playlist);
                    });
                    return musicCard;
                })
                .toList();

            Platform.runLater(() -> {
                this.playlistGrid.getChildren().clear();
                GridUtils.addInColumns(this.playlistGrid, musicCards);
                this.node.getChildren().set(1, this.contentPane);
                this.createPlaylistButton.setVisible(true);
            });
        }
    }
}
