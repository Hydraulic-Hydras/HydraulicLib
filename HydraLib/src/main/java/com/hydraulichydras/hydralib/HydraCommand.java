package com.hydraulichydras.hydralib;

import java.util.Set;

/*
 * A simple Command state machine that represents actions which are performed by the robot.
 */
public interface HydraCommand {

    /**
     * Called once when the command is initially scheduled.
     */
    void initialize();

    /**
     * Called while the command runs.
     */
    default void execute() {}

    /**
     * The action after the command ends or cancel it.
     */
    default void end(boolean interrupted) {}

    /**
     * To determine if the command has finished. 
     * 
     * @return whether the command has finished.
     */
    default boolean isFinished() {
        return false;
    }

    Set<HydraSubsystem> getRequirements();

    default boolean hasRequirement(HydraSubsystem requirement) {
        return getRequirements().contains(requirement);
    }

    default void schedule(boolean interruptible) {
        HydraCommandMachine.getInstance().schedule(interruptible, this);
    }

    default void schedule() {
        schedule(true);
    }

    default void cancel() {
        HydraCommandMachine.getInstance().cancel(this);
    }

    default boolean isScheduled() {
        return HydraCommandMachine.getInstance().isScheduled(this);
    }

    default boolean runsWhenDisabled() {
        return false;
    }
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
