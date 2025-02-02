package org.saartako.client.components;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;

/**
 * Identical to a toggle button, but cannot be unselected unless another toggle in the group is selected
 */
public class RequiredToggleButton extends ToggleButton {

    @Override
    public void fire() {
        if (!isDisabled() && !isSelected()) {
            setSelected(!isSelected());
            fireEvent(new ActionEvent());
        }
    }
}
