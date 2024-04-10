package com.hydraulichydras.hydralib;

import java.util.function.BooleanSupplier;

/**
 * Class for reading the state of a Hydra controller button.
 */
public class HydraSwitchReader {

    /** Last state of the button **/
    private boolean lastState;

    /** Current state of the button **/
    private boolean currState;

    /** The state of the button **/
    private final BooleanSupplier buttonState;

    /**
     * Initializes controller variables based on the Hydra PS controller and the button.
     *
     * @param controller The controller joystick.
     * @param button     The controller button.
     **/
    public HydraSwitchReader(HydraPSController controller, HydraSwitches.HydraButton button) {
        buttonState = () -> controller.getButton(button);
        currState = buttonState.getAsBoolean();
        lastState = currState;
    }

    /**
     * Initializes controller variables based on a custom BooleanSupplier for button value.
     *
     * @param buttonValue The BooleanSupplier providing the button value.
     **/
    public HydraSwitchReader(BooleanSupplier buttonValue) {
        buttonState = buttonValue;
        currState = buttonState.getAsBoolean();
        lastState = currState;
    }

    /**
     * Reads the button value.
     **/
    public void readValue() {
        lastState = currState;
        currState = buttonState.getAsBoolean();
    }

    /**
     * Checks if the button is currently pressed.
     *
     * @return True if the button is pressed, false otherwise.
     **/
    public boolean isDown() {
        return buttonState.getAsBoolean();
    }

    /**
     * Checks if the button was just pressed.
     *
     * @return True if the button was just pressed, false otherwise.
     **/
    public boolean wasJustPressed() {
        return (!lastState && currState);
    }

    /**
     * Checks if the button was just released.
     *
     * @return True if the button was just released, false otherwise.
     **/
    public boolean wasJustReleased() {
        return (lastState && !currState);
    }

    /**
     * Checks if the button state has changed since the last read.
     *
     * @return True if the button state has changed, false otherwise.
     **/
    public boolean stateJustChanged() {
        return (lastState != currState);
    }

}