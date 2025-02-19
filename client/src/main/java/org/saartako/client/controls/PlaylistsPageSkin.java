package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.constants.Route;
import org.saartako.client.events.ListItemClickEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.utils.PlaylistUtils;
import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;

import java.util.List;
import java.util.Optional;

public class PlaylistsPageSkin implements Skin<PlaylistsPage> {

    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final PlaylistsPage control;

    private final ChangeListener<ObservableList<Playlist>> playlistsChangeListener;

    private final CustomTextField searchTextField = new CustomTextField();

    private final MusicCardGrid musicCardGrid = new MusicCardGrid();

    private final ScrollPane contentPane = new ScrollPane(this.musicCardGrid);

    private final Loader loader = new Loader();

    private final Button createPlaylistButton = new Button("Create new playlist");

    private final VBox node = new VBox(16);

    public PlaylistsPageSkin(PlaylistsPage control) {
        this.control = control;

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
            final Playlist playlist = this.playlistService.getPlaylists().get(index);

            this.playlistService.setCurrentPlaylist(playlist);
            this.routerService.setCurrentRoute(Route.PLAYLIST_VIEW);
        });

        this.createPlaylistButton.getStyleClass().add(Styles.ACCENT);
        this.createPlaylistButton.setOnAction(event -> {
            final Optional<CreatePlaylistDTO> result = openCreatePlaylistDialog();
            result.ifPresent(this.playlistService::createPlaylist);
        });
        this.node.getChildren().add(this.createPlaylistButton);

        this.searchTextField.textProperty().addListener((o, prev, search) ->
            updatePlaylists(this.playlistService.getPlaylists(), search));
        this.playlistsChangeListener = (o, prev, playlists) ->
            updatePlaylists(playlists, this.searchTextField.getText());
        this.playlistService.playlistsProperty().addListener(this.playlistsChangeListener);

        updatePlaylists(this.playlistService.getPlaylists(), this.searchTextField.getText());
    }

    private Optional<CreatePlaylistDTO> openCreatePlaylistDialog() {
        final Dialog<CreatePlaylistDTO> dialog = new Dialog<>();
        dialog.setTitle("Create new playlist");

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(8));
        gridPane.setHgap(8);
        gridPane.setVgap(8);

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

    private void updatePlaylists(ObservableList<Playlist> playlists, String search) {
        if (playlists == null) {
            Platform.runLater(() -> {
                this.node.getChildren().set(1, this.loader);
                this.createPlaylistButton.setVisible(false);
            });
        } else {
            final List<? extends Playlist> filtered = this.playlistService.filterPlaylists(playlists, search);

            final List<CardItem> cardItems = filtered.stream().map(PlaylistUtils::playlistToCardItem).toList();

            Platform.runLater(() -> {
                this.musicCardGrid.cardItemsProperty().setAll(cardItems);
                this.node.getChildren().set(1, this.contentPane);
                this.createPlaylistButton.setVisible(true);
            });
        }
    }

    @Override
    public PlaylistsPage getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
        this.playlistService.playlistsProperty().removeListener(this.playlistsChangeListener);
    }
}
