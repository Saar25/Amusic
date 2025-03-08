package org.saartako.client.controls;

import javafx.scene.control.Control;
import org.saartako.client.models.RouteNode;

public class UploadSongPage extends Control implements RouteNode {

    @Override
    protected UploadSongPageSkin createDefaultSkin() {
        return new UploadSongPageSkin(this);
    }
}
