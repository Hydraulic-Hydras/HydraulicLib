package com.hydraulichydras.hydralib;

import java.util.Set;

/*
 * A simple Command state machine that represents actions which are performed by the robot.
 */
public interface HydraCommand {

    /**
     * Called once when the command is initially scheduled.
     */
    default void initialize() {}

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

    /**
     * Retrieves the set of subsystems required by this command.
     *
     * @return the set of subsystems required by this command.
     */
    Set<HydraSubsystem> getRequirements();

    /**
     * Creates a new command to be executed when this command is finished.
     *
     * @param toRun the action to run when this command is finished.
     * @return a new command to be executed when this command is finished.
     */
    default HydraCommand whenFinished(Runnable toRun) {
        return new HydraSerialCommand(this, new HydraDirectCommand(toRun));
    }

    /**
     * Creates a new command to be executed before this command starts.
     *
     * @param toRun the action to run before this command starts.
     * @return a new command to be executed before this command starts.
     */
    default HydraCommand beforeStarting(Runnable toRun) {
        return new HydraSerialCommand(new HydraDirectCommand(toRun), this);
    }

    /**
     * Chains this command with the given commands to be executed sequentially.
     *
     * @param next the commands to be executed after this command.
     * @return a new command representing the sequential execution of this command followed by the given commands.
     */
    default HydraCommand afterThat(HydraCommand... next) {
        HydraSerialCommand group = new HydraSerialCommand(this);
        group.addCommands(next);
        return group;
    }

    /**
     * Chains this command with the given commands to be executed in parallel.
     *
     * @param parallel the commands to be executed in parallel with this command.
     * @return a new command representing the parallel execution of this command with the given commands.
     */
    default HydraCommand alongWith(HydraCommand... parallel) {
        HydraCollateralCommand group = new HydraCollateralCommand(this);
        group.addCommands(parallel);
        return group;
    }

    /**
     * Checks if this command requires a specific subsystem.
     *
     * @param requirement the subsystem to check for requirement.
     * @return true if this command requires the specified subsystem, false otherwise.
     */
    default boolean hasRequirement(HydraSubsystem requirement) {
        return getRequirements().contains(requirement);
    }

    /**
     * Schedules this command to be executed by the command machine.
     *
     * @param disrupt true if the command can be interrupted, false otherwise.
     */
    default void schedule(boolean disrupt) {
        HydraCommandMachine.getInstance().schedule(disrupt, this);
    }

    /**
     * Schedules this command to be executed by the command machine with interruptible set to true.
     */
    default void schedule() {
        schedule(true);
    }

    /**
     * Cancels the execution of this command.
     */
    default void cancel() {
        HydraCommandMachine.getInstance().cancel(this);
    }

    /**
     * Checks if this command is currently scheduled for execution.
     *
     * @return true if this command is scheduled, false otherwise.
     */
    default boolean isScheduled() {
        return HydraCommandMachine.getInstance().isScheduled(this);
    }

    /**
     * Checks if this command should run when the robot is disabled.
     *
     * @return true if this command should run when the robot is disabled, false otherwise.
     */
    default boolean runsWhenDisabled() {
        return false;
    }

    /**
     * Retrieves the name of this command class.
     *
     * @return the name of this command class.
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }
}