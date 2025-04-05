package org.saartako.client.controls;

import javafx.beans.binding.ListBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.saartako.client.Config;
import org.saartako.client.enums.Route;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.utils.BindingsUtils;
import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;

import java.util.Optional;

/**
 * Playlists page control, under route PLAYLISTS, showing all the playlists fetched by the service
 */
public class PlaylistsPage extends Control implements RouteNode {

    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final ObjectProperty<String> playlistsFilter = new SimpleObjectProperty<>(this, "songsFilter", "");

    private final ListBinding<Playlist> filteredPlaylists = BindingsUtils.createJavaListBinding(() -> {
        final ObservableList<Playlist> playlists = this.playlistService.playlistsProperty();
        if (playlists == null) return null;
        final String filter = this.playlistsFilter.get();
        if (filter == null) return playlists;
        return this.playlistService.filterPlaylists(playlists, filter);
    }, this.playlistService.playlistsProperty(), this.playlistsFilter);

    @Override
    protected PlaylistsPageSkin createDefaultSkin() {
        return new PlaylistsPageSkin(this);
    }

    @Override
    public void onEnterView() {
        this.playlistService.fetchData();
    }

    public ObjectProperty<String> playlistsFilterProperty() {
        return this.playlistsFilter;
    }

    public ListBinding<Playlist> filteredPlaylistsProperty() {
        return this.filteredPlaylists;
    }

    public void onExpandPlaylist(Playlist song) {
        this.playlistService.setCurrentPlaylist(song);
        this.routerService.push(Route.PLAYLIST_VIEW);
    }

    public void onCreatePlaylistButtonClick() {
        final Optional<CreatePlaylistDTO> result = openCreatePlaylistDialog();
        result.ifPresent(this.playlistService::createPlaylist);
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
                    isPrivateCheckBox.isSelected()
                );
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
