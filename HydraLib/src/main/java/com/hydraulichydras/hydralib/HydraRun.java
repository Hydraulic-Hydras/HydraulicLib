package com.hydraulichydras.hydralib;

import java.util.function.BooleanSupplier;

/**
 * This class represents a trigger that activates commands based on a condition.
 */
public class HydraRun {

    private final BooleanSupplier m_isActive;

    /**
     * Constructs a new HydraRun with the given condition determining its activation.
     *
     * @param isActive the condition determining if the trigger should be active
     */
    public HydraRun(BooleanSupplier isActive) {
        m_isActive = isActive;
    }

    /**
     * Constructs a new HydraRun that is always inactive.
     * Useful as a no-argument constructor for subclasses overriding {@link #get()}.
     */
    public HydraRun() {
        m_isActive = () -> false;
    }

    /**
     * Returns whether the trigger is currently active.
     *
     * @return true if the trigger is active, false otherwise
     */
    public boolean get() {
        return m_isActive.getAsBoolean();
    }

    /**
     * Starts the given command whenever the trigger becomes active.
     *
     * @param command       the command to start
     * @param disrupt whether the command is interruptible
     * @return this trigger for method chaining
     */
    public HydraRun whenActive(final HydraCommand command, boolean disrupt) {
        HydraCommandMachine.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    command.schedule(disrupt);
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    /**
     * Starts the given command whenever the trigger becomes active.
     * The command is set to be interruptible.
     *
     * @param command the command to start
     * @return this trigger for method chaining
     */
    public HydraRun whenActive(final HydraCommand command) {
        return whenActive(command, true);
    }

    /**
     * Runs the given runnable whenever the trigger becomes active.
     *
     * @param toRun the runnable to run
     * @return this trigger for method chaining
     */
    public HydraRun whenActive(final Runnable toRun) {
        return whenActive(new HydraDirectCommand(toRun));
    }

    /**
     * Constantly starts the given command while the trigger is active.
     *
     * @param command       the command to start
     * @param disrupt whether the command is interruptible
     * @return this trigger for method chaining
     */
    public HydraRun whileActiveContinuous(final HydraCommand command, boolean disrupt) {
        HydraCommandMachine.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (pressed) {
                    command.schedule(disrupt);
                } else if (pressedLast) {
                    command.cancel();
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    /**
     * Constantly starts the given command while the trigger is active.
     * The command is set to be interruptible.
     *
     * @param command the command to start
     * @return this trigger for method chaining
     */
    public HydraRun whileActiveContinuous(final HydraCommand command) {
        return whileActiveContinuous(command, true);
    }

    /**
     * Constantly runs the given runnable while the trigger is active.
     *
     * @param toRun the runnable to run
     * @return this trigger for method chaining
     */
    public HydraRun whileActiveContinuous(final Runnable toRun) {
        return whileActiveContinuous(new HydraDirectCommand(toRun));
    }

    /**
     * Starts the given command when the trigger initially becomes active, and ends it when it becomes
     * inactive, but does not re-start it in-between.
     *
     * @param command       the command to start
     * @param disrupt whether the command is interruptible
     * @return this trigger for method chaining
     */
    public HydraRun whileActiveOnce(final HydraCommand command, boolean disrupt) {
        HydraCommandMachine.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    command.schedule(disrupt);
                } else if (pressedLast && !pressed) {
                    command.cancel();
                }

                pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Starts the given command when the trigger initially becomes active, and ends it when it becomes
     * inactive, but does not re-start it in-between. The command is set to be interruptible.
     *
     * @param command the command to start
     * @return this trigger for method chaining
     */
    public HydraRun whileActiveOnce(final HydraCommand command) {
        return whileActiveOnce(command, true);
    }

    /**
     * Starts the command when the trigger becomes inactive.
     *
     * @param command       the command to start
     * @param disrupt whether the command is interruptible
     * @return this trigger for method chaining
     */
    public HydraRun whenInactive(final HydraCommand command, boolean disrupt) {
        HydraCommandMachine.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (pressedLast && !pressed) {
                    command.schedule(disrupt);
                }

                pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Starts the command when the trigger becomes inactive. The command is set to be interruptible.
     *
     * @param command the command to start
     * @return this trigger for method chaining
     */
    public HydraRun whenInactive(final HydraCommand command) {
        return whenInactive(command, true);
    }

    /**
     * Runs the given runnable when the trigger becomes inactive.
     *
     * @param toRun the runnable to run
     * @return this trigger for method chaining
     */
    public HydraRun whenInactive(final Runnable toRun) {
        return whenInactive(new HydraDirectCommand(toRun));
    }

    /**
     * Toggles a command when the trigger becomes active.
     *
     * @param command       the command to toggle
     * @param interruptible whether the command is interruptible
     * @return this trigger for method chaining
     */
    public HydraRun toggleWhenActive(final HydraCommand command, boolean interruptible) {
        HydraCommandMachine.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    if (command.isScheduled()) {
                        command.cancel();
                    } else {
                        command.schedule(interruptible);
                    }
                }

                pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Toggles a command when the trigger becomes active. The command is set to be interruptible.
     *
     * @param command the command to toggle
     * @return this trigger for method chaining
     */
    public HydraRun toggleWhenActive(final HydraCommand command) {
        return toggleWhenActive(command, true);
    }

    /**
     * Toggles between two commands when the trigger becomes active (commandOne then commandTwo
     * then commandOne).
     *
     * @param commandOne    the command to toggle
     * @param commandTwo    the command to be toggled
     * @param disrupt whether the commands are interruptible
     * @return this trigger for method chaining
     */
    public HydraRun toggleWhenActive(final HydraCommand commandOne, final HydraCommand commandTwo, boolean disrupt) {
        HydraCommandMachine.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();
            private boolean isfirstActive = false;

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    if (isfirstActive) {
                        if (commandOne.isScheduled()) {
                            commandOne.cancel();
                        }
                        commandTwo.schedule(disrupt);
                    } else {
                        if (commandTwo.isScheduled()) {
                            commandTwo.cancel();
                        }
                        commandOne.schedule(disrupt);
                    }

                    isfirstActive = !isfirstActive;
                }

                pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Toggles between two commands when the trigger becomes active (commandOne then commandTwo
     * then commandOne). These commands are set to be interruptible.
     *
     * @param commandOne the command to start
     * @param commandTwo the command to be activated after
     * @return this trigger for method chaining
     */
    public HydraRun toggleWhenActive(final HydraCommand commandOne, final HydraCommand commandTwo) {
        return toggleWhenActive(commandOne, commandTwo, true);
    }

    /**
     * Toggles between two runnables when the trigger becomes active (runnableOne then runnableTwo
     * then runnableOne). These runnables are set to be interruptible.
     *
     * @param runnableOne the runnable to start
     * @param runnableTwo the runnable to be activated after
     * @return this trigger for method chaining
     */
    public HydraRun toggleWhenActive(final Runnable runnableOne, final Runnable runnableTwo) {
        return toggleWhenActive(new HydraDirectCommand(runnableOne), new HydraDirectCommand(runnableTwo));
    }

    /**
     * Cancels a command when the trigger becomes active.
     *
     * @param command the command to cancel
     * @return this trigger for method chaining
     */
    public HydraRun cancelWhenActive(final HydraCommand command) {
        HydraCommandMachine.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    command.cancel();
                }

                pressedLast = pressed;
            }
        });
        return this;
    }
    /**
     * Composes this button with another button, returning a new button that is active when both
     * buttons are active.
     *
     * @param button the button to compose with
     * @return the button that is active when both buttons are active
     */
    public HydraRun and(HydraRun button) {
        return new HydraRun(() -> get() && button.get());
    }

    /**
     * Composes this button with another button, returning a new button that is active when either
     * button is active.
     *
     * @param button the button to compose with
     * @return the button that is active when either button is active
     */
    public HydraRun or(HydraRun button) {
        return new HydraRun(() -> get() || button.get());
    }

    /**
     * Creates a new button that is active when this button is inactive, i.e. that acts as the
     * negation of this button.
     *
     * @return the negated button
     */
    public HydraRun negate() {
        return new HydraRun(() -> !get());
    }
}
