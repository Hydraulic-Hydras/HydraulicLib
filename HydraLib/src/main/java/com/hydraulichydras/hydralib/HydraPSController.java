package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class for reading inputs from a Hydra PS controller.
 */
public class HydraPSController {

    /** The gamepad instance to read inputs from */
    public Gamepad gamepad;

    /** Map to store switch readers for each button */
    public HashMap<HydraSwitches.HydraButton, HydraSwitchReader> switchReader;

    /** Map to store controller readers for each button */
    public HashMap<HydraSwitches.HydraButton, HydraControllerReader> controllerReader;

    /** Array of all Hydra buttons */
    private final HydraSwitches.HydraButton[] buttons = {
            HydraSwitches.HydraButton.CIRCLE, HydraSwitches.HydraButton.CROSS,
            HydraSwitches.HydraButton.SQUARE, HydraSwitches.HydraButton.TRIANGLE,
            HydraSwitches.HydraButton.LEFT_BUMPER, HydraSwitches.HydraButton.RIGHT_BUMPER,
            HydraSwitches.HydraButton.SHARE, HydraSwitches.HydraButton.OPTIONS,
            HydraSwitches.HydraButton.DPAD_UP, HydraSwitches.HydraButton.DPAD_DOWN,
            HydraSwitches.HydraButton.DPAD_LEFT, HydraSwitches.HydraButton.DPAD_RIGHT,
            HydraSwitches.HydraButton.LEFT_STICK_BUTTON, HydraSwitches.HydraButton.RIGHT_STICK_BUTTON
    };

    /**
     * Constructor to initialize the HydraPSController with a gamepad instance.
     *
     * @param gamepad The gamepad instance to read inputs from.
     */
    public HydraPSController(Gamepad gamepad) {
        this.gamepad = gamepad;
        switchReader = new HashMap<>();
        controllerReader = new HashMap<>();

        // Initialize switch readers and controller readers for each button
        for (HydraSwitches.HydraButton button : buttons) {
            switchReader.put(button, new HydraSwitchReader(this, button));
            controllerReader.put(button, new HydraControllerReader(this, button));
        }
    }

    /**
     * Get the state of a specific button.
     *
     * @param button The button to get the state of.
     * @return The state of the button.
     */
    public boolean getButton(HydraSwitches.HydraButton button) {
        boolean buttonValue = false;
        // Retrieve the state of the specified button from the gamepad
        switch (button) {
            case SQUARE:
                buttonValue = gamepad.square;
                break;
            case TRIANGLE:
                buttonValue = gamepad.triangle;
                break;
            case CROSS:
                buttonValue = gamepad.cross;
                break;
            case CIRCLE:
                buttonValue = gamepad.circle;
                break;
            case LEFT_BUMPER:
                buttonValue = gamepad.left_bumper;
                break;
            case RIGHT_BUMPER:
                buttonValue = gamepad.right_bumper;
                break;
            case DPAD_UP:
                buttonValue = gamepad.dpad_up;
                break;
            case DPAD_DOWN:
                buttonValue = gamepad.dpad_down;
                break;
            case DPAD_LEFT:
                buttonValue = gamepad.dpad_left;
                break;
            case DPAD_RIGHT:
                buttonValue = gamepad.dpad_right;
                break;
            case SHARE:
                buttonValue = gamepad.share;
                break;
            case OPTIONS:
                buttonValue = gamepad.options;
                break;
            case LEFT_STICK_BUTTON:
                buttonValue = gamepad.left_stick_button;
                break;
            case RIGHT_STICK_BUTTON:
                buttonValue = gamepad.right_stick_button;
                break;
            default:
                buttonValue = false;
                break;
        }
        return buttonValue;
    }

    // Similar Javadoc comments can be applied to other methods as needed

    /**
     * Get the state of a specific trigger.
     *
     * @param trigger The trigger to get the state of.
     * @return The state of the trigger.
     */
    public double getTrigger(HydraSwitches.HydraTrigger trigger) {
        double triggerValue = 0;
        // Retrieve the state of the specified trigger from the gamepad
        switch (trigger) {
            case LEFT_TRIGGER:
                triggerValue = gamepad.left_trigger;
                break;
            case RIGHT_TRIGGER:
                triggerValue = gamepad.right_trigger;
                break;
            default:
                break;
        }
        return triggerValue;
    }

    /**
     * Returns the Y-value of the left analog stick.
     *
     * @return The Y-value of the left analog stick.
     */
    public double getLeftY() {
        return -gamepad.left_stick_y;
    }

    /**
     * Returns the Y-value of the right analog stick.
     *
     * @return The Y-value of the right analog stick.
     */
    public double getRightY() {
        return gamepad.right_stick_y;
    }

    /**
     * Returns the X-value of the left analog stick.
     *
     * @return The X-value of the left analog stick.
     */
    public double getLeftX() {
        return gamepad.left_stick_x;
    }

    /**
     * Returns the X-value of the right analog stick.
     *
     * @return The X-value of the right analog stick.
     */
    public double getRightX() {
        return gamepad.right_stick_x;
    }

    /**
     * Checks if the specified button was just pressed.
     *
     * @param button The button to check.
     * @return True if the button was just pressed, false otherwise.
     */
    public boolean wasJustPressed(HydraSwitches.HydraButton button) {
        return Objects.requireNonNull(switchReader.get(button)).wasJustPressed();
    }

    /**
     * Checks if the specified button was just released.
     *
     * @param button The button to check.
     * @return True if the button was just released, false otherwise.
     */
    public boolean wasJustReleased(HydraSwitches.HydraButton button) {
        return Objects.requireNonNull(switchReader.get(button)).wasJustReleased();
    }

    /**
     * Updates the state of all buttons.
     * Call this method once per loop iteration.
     */
    public void readButtons() {
        for (HydraSwitches.HydraButton button : buttons) {
            Objects.requireNonNull(switchReader.get(button)).readValue();
        }
    }

    /**
     * Checks if the specified button is currently pressed.
     *
     * @param button The button to check.
     * @return True if the button is currently pressed, false otherwise.
     */
    public boolean isDown(HydraSwitches.HydraButton button) {
        return Objects.requireNonNull(switchReader.get(button)).isDown();
    }

    /**
     * Checks if the state of the specified button has changed since the last read.
     *
     * @param button The button to check.
     * @return True if the state of the button has changed, false otherwise.
     */
    public boolean stateJustChanged(HydraSwitches.HydraButton button) {
        return Objects.requireNonNull(switchReader.get(button)).stateJustChanged();
    }

    /**
     * Retrieves the controller reader for the specified button.
     *
     * @param button The button to retrieve the controller reader for.
     * @return The controller reader for the specified button.
     */
    public HydraControllerReader getControllerButton(HydraSwitches.HydraButton button) {
        return controllerReader.get(button);
    }

}
