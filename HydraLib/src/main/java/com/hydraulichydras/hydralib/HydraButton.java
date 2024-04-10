package com.hydraulichydras.hydralib;

import java.util.function.BooleanSupplier;

/**
 * Represents a button on a Hydra controller.
 */
public class HydraButton extends HydraRun {

    /** The condition supplier determining the button's activation state. */
    private final BooleanSupplier H_Active;

    /**
     * Constructs a new HydraButton that is initially inactive.
     */
    public HydraButton() {
        this(() -> false);
    }

    /**
     * Constructs a new HydraButton with the given condition for activation.
     *
     * @param isPressed the condition for activation
     */
    public HydraButton(BooleanSupplier isPressed) {
        H_Active = isPressed;
    }

    /**
     * Gets the current state of the button.
     *
     * @return true if the button is currently pressed, false otherwise
     */
    public boolean get() {
        return H_Active.getAsBoolean();
    }

    /**
     * Specifies a command to execute when the button is pressed.
     *
     * @param command the command to execute
     * @param disrupt whether the command can be interrupted
     * @return this HydraButton for method chaining
     */
    public HydraButton whenPressed(final HydraCommand command, boolean disrupt) {
        whenActive(command, disrupt);
        return this;
    }

    /**
     * Specifies a command to execute when the button is pressed, with interruptible default value.
     *
     * @param command the command to execute
     * @return this HydraButton for method chaining
     */
    public HydraButton whenPressed(final HydraCommand command) {
        whenActive(command);
        return this;
    }

    /**
     * Specifies a runnable to execute when the button is pressed.
     *
     * @param toRun the runnable to execute
     */
    public void whenPressed(final Runnable toRun) {
        whenActive(toRun);
    }

    /**
     * Specifies a command to execute continuously while the button is held down,
     * and another command to execute when the button is released.
     *
     * @param command the command to execute while the button is held down
     * @param disrupt whether the command execution can be interrupted
     * @return this HydraButton for method chaining
     */
    public HydraButton whileHeld(final HydraCommand command, boolean disrupt) {
        whileActiveContinuous(command, disrupt);
        return this;
    }


    /**
     * Specifies a command to execute continuously while the button is held down, with interruptible default value,
     * and another command to execute when the button is released.
     *
     * @param command the command to execute while the button is held down
     * @return this HydraButton for method chaining
     */
    public HydraButton whileHeld(final HydraCommand command) {
        whileActiveContinuous(command);
        return this;
    }

    /**
     * Specifies a runnable to execute continuously while the button is held down.
     *
     * @param toRun the runnable to execute while the button is held down
     * @return this HydraButton for method chaining
     */
    public HydraButton whileHeld(final Runnable toRun) {
        whileActiveContinuous(toRun);
        return this;
    }

    /**
     * Specifies a command to execute once when the button transitions from not held to held, and
     * another command to execute when the button transitions from held to not held.
     *
     * @param command the command to execute when the button is held
     * @param disrupt whether the command execution can be interrupted
     * @return this HydraButton for method chaining
     */
    public HydraButton whenHeld(final HydraCommand command, boolean disrupt) {
        whileActiveOnce(command, disrupt);
        return this;
    }

    /**
     * Specifies a command to execute once when the button transitions from not held to held,
     * with interruptible default value, and another command to execute when the button transitions from held to not held.
     *
     * @param command the command to execute when the button is held
     * @return this HydraButton for method chaining
     */
    public HydraButton whenHeld(final HydraCommand command) {
        whileActiveOnce(command, true);
        return this;
    }

    /**
     * Specifies a command to execute once when the button is released.
     *
     * @param command the command to execute when the button is released
     * @param disrupt whether the command execution can be interrupted
     * @return this HydraButton for method chaining
     */
    public HydraButton whenReleased(final HydraCommand command, boolean disrupt) {
        whenInactive(command, disrupt);
        return this;
    }

    /**
     * Specifies a command to execute once when the button is released, with interruptible default value.
     *
     * @param command the command to execute when the button is released
     * @return this HydraButton for method chaining
     */
    public HydraButton whenReleased(final HydraCommand command) {
        whenInactive(command);
        return this;
    }

    /**
     * Specifies a runnable to execute once when the button is released.
     *
     * @param toRun the runnable to execute when the button is released
     * @return this HydraButton for method chaining
     */
    public HydraButton whenReleased(final Runnable toRun) {
        whenInactive(toRun);
        return this;
    }

    /**
     * Toggles between two commands when the button is pressed.
     *
     * @param command the first command to toggle
     * @param disrupt whether the command execution can be interrupted
     * @return this HydraButton for method chaining
     */
    public HydraButton toggleWhenPressed(final HydraCommand command, boolean disrupt) {
        toggleWhenActive(command, disrupt);
        return this;
    }

    /**
     * Toggles between two commands when the button is pressed, with interruptible default value.
     *
     * @param command the first command to toggle
     * @return this HydraButton for method chaining
     */
    public HydraButton toggleWhenPressed(final HydraCommand command) {
        toggleWhenActive(command);
        return this;
    }
    /**
     * Toggles between two commands when the button is pressed.
     *
     * @param commandOne the first command to toggle
     * @param commandTwo the second command to toggle
     * @param disrupt whether the command execution can be interrupted
     * @return this HydraButton for method chaining
     */
    public HydraButton toggleWhenPressed(final HydraCommand commandOne, final HydraCommand commandTwo, boolean disrupt) {
        toggleWhenActive(commandOne, commandTwo, disrupt);
        return this;
    }


    /**
     * Toggles between two commands when the button is pressed, with interruptible default value.
     *
     * @param commandOne the first command to toggle
     * @param commandTwo the second command to toggle
     * @return this HydraButton for method chaining
     */
    public HydraButton toggleWhenPressed(final HydraCommand commandOne, final HydraCommand commandTwo) {
        toggleWhenActive(commandOne, commandTwo);
        return this;
    }


    /**
     * Toggles between two runnables when the button is pressed.
     *
     * @param runnableOne the first runnable to toggle
     * @param runnableTwo the second runnable to toggle
     * @return this HydraButton for method chaining
     */
    public HydraButton toggleWhenPressed(final Runnable runnableOne, final Runnable runnableTwo) {
        toggleWhenActive(runnableOne, runnableTwo);
        return this;
    }

    /**
     * Cancels a command when the button is pressed.
     *
     * @param command the command to cancel when the button is pressed
     * @return this HydraButton for method chaining
     */
    public HydraButton cancelWhenPressed(final HydraCommand command) {
        cancelWhenActive(command);
        return this;
    }
}
