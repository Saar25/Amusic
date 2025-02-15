package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.utils.ColorUtils;
import org.saartako.playlist.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlaylistsPageSkin implements Skin<PlaylistsPage> {

    private final PlaylistService playlistService = PlaylistService.getInstance();

    private final PlaylistsPage control;

    private final ChangeListener<ObservableList<Playlist>> playlistsChangeListener;

    private final CustomTextField searchTextField = new CustomTextField();
    private final MusicCardGrid musicCardGrid = new MusicCardGrid();
    private final Label loadingLabel = new Label("Loading...");

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

        this.searchTextField.textProperty().addListener((o, prev, search) ->
            updatePlaylists(this.playlistService.getPlaylists(), search));
        this.playlistsChangeListener = (o, prev, playlists) ->
            updatePlaylists(playlists, this.searchTextField.getText());
        this.playlistService.playlistsProperty().addListener(this.playlistsChangeListener);

        updatePlaylists(this.playlistService.getPlaylists(), this.searchTextField.getText());
    }

    private void updatePlaylists(ObservableList<Playlist> playlists, String search) {
        if (playlists == null) {
            this.node.getChildren().set(1, this.loadingLabel);
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
