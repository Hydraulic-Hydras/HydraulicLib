package com.hydraulichydras.hydralib;

/*
 * A basic Subsytem class to be used with Command Sequences. Subsystems are also another form of HydraContraption.
 */
public abstract class HydraSubsystem {
    
    /**
     * This method is called periodically, usefil for updating specific-Subsytem states.
     */
    public abstract void periodic();

    /**
     * Reads the states of the Subsystem.
     */
    public abstract void read();

    /**
     * Allows the Subsystem to obtain power to perform motion.
     */
    public abstract void write();

    /**
     * Resets all states.
     */
    public abstract void reset();

}
