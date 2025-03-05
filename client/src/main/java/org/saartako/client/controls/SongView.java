package org.saartako.client.controls;

import javafx.scene.control.Control;
import org.saartako.client.models.RouteNode;

public class SongView extends Control implements RouteNode {

    @Override
    protected SongViewSkin createDefaultSkin() {
        return new SongViewSkin(this);
    }

    @Override
    public void onExistView() {
        System.out.println("exit");
    }

    @Override
    public void onEnterView() {
        System.out.println("yay");
    }
}
