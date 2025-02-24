package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.constants.Route;
import org.saartako.client.events.CardItemEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.utils.GridUtils;
import org.saartako.client.utils.PlaylistUtils;
import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;

import java.util.List;
import java.util.Optional;

public class PlaylistsPageSkin extends SkinBase<PlaylistsPage> {

    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final CustomTextField searchTextField = new CustomTextField();

    private final GridPane playlistGrid = new GridPane();

    private final ScrollPane contentPane = new ScrollPane(this.playlistGrid);

    private final Loader loader = new Loader();

    private final Button createPlaylistButton = new Button("Create new playlist");

    private final VBox node = new VBox(Config.GAP_LARGE);

    public PlaylistsPageSkin(PlaylistsPage control) {
        super(control);

        this.node.setAlignment(Pos.TOP_CENTER);
        this.node.setPadding(new Insets(Config.GAP_MEDIUM, Config.GAP_HUGE, Config.GAP_MEDIUM, Config.GAP_HUGE));

        this.searchTextField.setPromptText("Search");
        this.searchTextField.setMaxWidth(300);
        this.searchTextField.setLeft(new FontIcon(Material2MZ.SEARCH));
        this.searchTextField.setRight(new FontIcon(Material2AL.CLEAR));
        this.node.getChildren().add(this.searchTextField);

        this.node.getChildren().add(this.loader);

        this.contentPane.setFitToWidth(true);

        VBox.setVgrow(this.loader, Priority.ALWAYS);
        VBox.setVgrow(this.contentPane, Priority.ALWAYS);

        GridUtils.initializeGrid(this.playlistGrid,
            Config.GRID_LARGE_COLUMNS, 0, Config.GAP_LARGE, Config.GAP_MEDIUM);

        this.createPlaylistButton.getStyleClass().add(Styles.ACCENT);
        this.createPlaylistButton.setOnAction(event -> {
            final Optional<CreatePlaylistDTO> result = openCreatePlaylistDialog();
            result.ifPresent(this.playlistService::createPlaylist);
        });
        this.node.getChildren().add(this.createPlaylistButton);

        registerChangeListener(this.searchTextField.textProperty(), observable -> updatePlaylists());
        registerListChangeListener(this.playlistService.playlistsProperty(), observable -> updatePlaylists());
        updatePlaylists();

        getChildren().setAll(this.node);
    }

    private Optional<CreatePlaylistDTO> openCreatePlaylistDialog() {
        final Dialog<CreatePlaylistDTO> dialog = new Dialog<>();
        dialog.setTitle("Create new playlist");

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(Config.GAP_MEDIUM));
        gridPane.setHgap(Config.GAP_MEDIUM);
        gridPane.setVgap(Config.GAP_MEDIUM);

        final Label playlistNameLabel = new Label("Enter playlist name:");
        final TextField playlistNameTextField = new TextField();
        playlistNameTextField.setPromptText("Enter playlist name...");
        gridPane.addRow(0, playlistNameLabel, playlistNameTextField);

        final Label isPrivateLabel = new Label("Is private:");
        final CheckBox isPrivateCheckBox = new CheckBox();
        gridPane.addRow(1, isPrivateLabel, isPrivateCheckBox);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(
            new ButtonType("Create", ButtonBar.ButtonData.OK_DONE),
            new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

        dialog.setResultConverter(button -> {
            if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new CreatePlaylistDTO(
                    playlistNameTextField.getText(),
                    isPrivateCheckBox.isSelected(),
                    false
                );
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void updatePlaylists() {
        updatePlaylists(this.playlistService.getPlaylists(), this.searchTextField.getText());
    }

    private void updatePlaylists(ObservableList<Playlist> playlists, String search) {
        if (playlists == null) {
            Platform.runLater(() -> {
                this.playlistGrid.getChildren().clear();
                this.node.getChildren().set(1, this.loader);
                this.createPlaylistButton.setVisible(false);
            });
        } else {
            final List<? extends Playlist> filtered = this.playlistService.filterPlaylists(playlists, search);

            final List<MusicCard> musicCards = filtered.stream()
                .map(playlist -> {
                    final CardItem cardItem = PlaylistUtils.playlistToCardItem(playlist);
                    final MusicCard musicCard = new MusicCard();
                    musicCard.setCardItem(cardItem);
                    musicCard.setExpandable(true);
                    musicCard.addEventFilter(CardItemEvent.EXPAND_CARD_ITEM, event -> {
                        this.playlistService.setCurrentPlaylist(playlist);
                        this.routerService.push(Route.PLAYLIST_VIEW);
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
