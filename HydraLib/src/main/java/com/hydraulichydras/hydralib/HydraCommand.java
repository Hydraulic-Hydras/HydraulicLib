package com.hydraulichydras.hydralib;

/*
 * A simple Command state machine that represents actions which are performed by the robot.
 */

@FunctionalInterface
public interface HydraCommand {
    
    /**
     * Called once when the command is initially scheduled.
     */
    public abstract void initialize();

    /**
     * Called while the command runs.
     */
    public abstract void execute();

    /**
     * The action after the command ends or cancel it.
     * 
     * @param interrupted
     */
    public void end(boolean interrupted);

    /**
     * To determine if the command has finished. 
     * 
     * @param interrupted
     * @return whether the command has finished.
     */
    default boolean isFinished(boolean interrupted) { 
        return false;
    }

}
