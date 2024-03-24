package com.hydraulichydras.hydralib;

/**
 * Represents the state of a Hydra command.
 */
public class HydraCommandState {

    // Flag indicating if the command is disrupted
    private final boolean disrupt;

    // Constructor to initialize the command state with disruptibility
    public HydraCommandState(boolean disrupt) {
        this.disrupt = disrupt;
    }

    // Method to check if the command is disrupted
    public boolean isDisrupted() {
        return disrupt;
    }
}