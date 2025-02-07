package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.services.SongService;
import org.saartako.song.Song;

import java.util.List;

public class SongsGridSkin implements Skin<SongsGrid> {

    private static final int COLUMN_COUNT = 3;

    private final SongService songService = SongService.getInstance();

    private final SongsGrid control;

    private final ScrollPane node = new ScrollPane();

    private final GridPane gridPane = new GridPane();

    public SongsGridSkin(SongsGrid control) {
        this.control = control;

        this.node.setFitToWidth(true);
        this.node.setPadding(new Insets(8, 40, 8, 40));

        final VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.TOP_CENTER);
        this.node.setContent(vBox);

        final CustomTextField searchTextField = new CustomTextField();
        searchTextField.setPromptText("Search");
        searchTextField.setMaxWidth(300);
        searchTextField.setLeft(new FontIcon(Material2MZ.SEARCH));
        searchTextField.setRight(new FontIcon(Material2AL.CLEAR));
        vBox.getChildren().add(searchTextField);

        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setPadding(new Insets(16));
        this.gridPane.setVgap(16);
        this.gridPane.setHgap(16);

        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / COLUMN_COUNT);
        cc.setHalignment(HPos.CENTER);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            this.gridPane.getColumnConstraints().add(cc);
        }

        vBox.getChildren().add(this.gridPane);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
            onSongsChange(this.control.getSongs(), newValue));
        this.control.songsProperty().addListener((observable, oldValue, newValue) ->
            onSongsChange(newValue, searchTextField.getText()));

        onSongsChange(this.control.getSongs(), searchTextField.getText());
    }

    private void onSongsChange(List<? extends Song> songs, String filter) {
        if (songs == null) {
            final Label label = new Label("Loading...");
            label.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLDER);
            GridPane.setColumnSpan(label, 3);
            this.gridPane.getChildren().setAll(label);
        } else {
            this.songService.filterSongsAsync(songs, filter).whenComplete((filtered, error) -> {
                if (filtered != null) {
                    Platform.runLater(() -> updateSongsInGrid(filtered));
                }
                if (error != null) {
                    System.err.println("Error: " + error.getMessage());
                }
            });
        }
    }

    private void updateSongsInGrid(List<? extends Song> songs) {
        this.gridPane.getChildren().clear();
        for (int i = 0; i < songs.size(); i++) {
            final SongCard songCard = new SongCard();
            songCard.setSong(songs.get(i));

            this.gridPane.add(songCard, i % 3, i / 3);
        }
    }

    @Override
    public SongsGrid getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
    }
}
