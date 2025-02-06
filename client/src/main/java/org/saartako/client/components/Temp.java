package org.saartako.client.components;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

public class Temp extends VBox {

    public Temp() {
        var card1 = new Card();
        card1.getStyleClass().add(Styles.ELEVATED_1);
        card1.setMinWidth(300);
        card1.setMaxWidth(300);

        var btn1 = new Button("sad");
        btn1.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        var header1 = new Tile(
            "Title",
            "This is a description",
            new Circle(30, Color.RED)
        );
        header1.setAction(btn1);
        card1.setHeader(header1);

        card1.setSubHeader(new Rectangle(300, 100, Color.GREEN));

        var text1 = new TextFlow(new Text("sadjadbkf akdsfkadshfk asdlkf kc"));
        text1.setMaxWidth(260);
        card1.setBody(text1);

// ~

        var card2 = new Card();
        card2.getStyleClass().add(Styles.ELEVATED_1);
        card2.setMinWidth(300);
        card2.setMaxWidth(300);

        var header2 = new Tile(
            "Reviewers",
            "Request up to 10 reviewers"
        );
        card2.setHeader(header2);

        var tf2 = new CustomTextField();
        tf2.setPromptText("Search people");
        tf2.setLeft(new FontIcon(Material2MZ.SEARCH));
        card2.setSubHeader(tf2);

        var body2 = new VBox(10);
        card2.setBody(body2);
        for (int i = 0; i < 5; i++) {
            var cb = new CheckBox();
            var lbl = new Label("asd");
            var circle = new Circle(
                8, Color.GRAY
            );

            var row = new HBox(10, circle, cb, lbl);
            row.setAlignment(Pos.CENTER_LEFT);
            body2.getChildren().add(row);
        }

        getChildren().addAll(card1, card2);
    }
}
