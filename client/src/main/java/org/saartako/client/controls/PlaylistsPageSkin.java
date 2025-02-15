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
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.utils.ColorUtils;
import org.saartako.playlist.Playlist;
import org.saartako.playlist.PlaylistDTO;

import java.util.*;

public class PlaylistsPageSkin implements Skin<PlaylistsPage> {

    private final PlaylistService playlistService = PlaylistService.getInstance();

    private final PlaylistsPage control;

    private final ChangeListener<ObservableList<Playlist>> playlistsChangeListener;

    private final CustomTextField searchTextField = new CustomTextField();
    private final MusicCardGrid musicCardGrid = new MusicCardGrid();
    private final Label loadingLabel = new Label("Loading...");
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

        this.loadingLabel.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLDER);
        this.node.getChildren().add(this.loadingLabel);

        VBox.setVgrow(this.loadingLabel, Priority.ALWAYS);
        VBox.setVgrow(this.musicCardGrid, Priority.ALWAYS);

        this.createPlaylistButton.getStyleClass().add(Styles.ACCENT);
        this.createPlaylistButton.setOnAction(event -> {
            final Optional<Playlist> playlist = openCreatePlaylistDialog();
            playlist.ifPresent(System.out::println);
        });
        this.node.getChildren().add(this.createPlaylistButton);

        this.searchTextField.textProperty().addListener((o, prev, search) ->
            updatePlaylists(this.playlistService.getPlaylists(), search));
        this.playlistsChangeListener = (o, prev, playlists) ->
            updatePlaylists(playlists, this.searchTextField.getText());
        this.playlistService.playlistsProperty().addListener(this.playlistsChangeListener);

        updatePlaylists(this.playlistService.getPlaylists(), this.searchTextField.getText());
    }

    private Optional<Playlist> openCreatePlaylistDialog() {
        final Dialog<Playlist> dialog = new Dialog<>();
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
                final PlaylistDTO playlistDTO = new PlaylistDTO();
                playlistDTO.setName(playlistNameTextField.getText());
                playlistDTO.setPrivate(isPrivateCheckBox.isSelected());
                return playlistDTO;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void updatePlaylists(ObservableList<Playlist> playlists, String search) {
        if (playlists == null) {
            Platform.runLater(() -> {
                this.node.getChildren().set(1, this.loadingLabel);
                this.createPlaylistButton.setVisible(false);
            });
        } else {
            final List<? extends Playlist> filtered = this.playlistService.filterPlaylists(playlists, search);

            final List<CardItem> cardItems = new ArrayList<>(filtered.stream().map(playlist -> {
                final Map<String, String> details = new TreeMap<>();
                if (playlist.getOwner() != null) {
                    details.put("By", playlist.getOwner().getDisplayName());
                }
                if (playlist.getSongs() != null) {
                    details.put("Songs", String.valueOf(playlist.getSongs().size()));
                }

                final Paint playlistColor = ColorUtils.getPlaylistColor(playlist);

                return new CardItem(playlist.getName(), details, playlistColor);
            }).toList());

            Platform.runLater(() -> {
                this.musicCardGrid.cardItemsProperty().setAll(cardItems);
                this.node.getChildren().set(1, this.musicCardGrid);
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
