package com.hydraulichydras.hydralib;

import androidx.annotation.NonNull;

/**
 * Class for reading the state of multiple buttons on a Hydra controller.
 */
public class HydraControllerReader extends HydraButton {

    /** The Hydra PS controller to read from **/
    private final HydraPSController controller;

    /** Array of Hydra buttons to read state from **/
    private final HydraSwitches.HydraButton[] H_buttons;

    /**
     * Initializes the HydraControllerReader with the specified controller and buttons.
     *
     * @param controller The Hydra PS controller.
     * @param button     The buttons to monitor.
     */
    public HydraControllerReader(HydraPSController controller, @NonNull HydraSwitches.HydraButton... button) {
        this.controller = controller;
        this.H_buttons = button;
    }

    /**
     * Gets the combined state of all monitored buttons.
     *
     * @return True if all monitored buttons are pressed, false otherwise.
     */
    @Override
    public boolean get() {
        boolean res = true;
        // Check state of each monitored button
        for (HydraSwitches.HydraButton button : H_buttons)
            res = res && controller.getButton(button);
        return res;
    }
}