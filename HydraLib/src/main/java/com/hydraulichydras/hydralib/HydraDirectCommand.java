package com.hydraulichydras.hydralib;

/**
 * Represents a direct command that executes a provided Runnable when initialized.
 */
public class HydraDirectCommand extends HydraCommandFoundation {

    // Runnable to be executed when the command is initialized
    private final Runnable toRun;

    // Constructor to initialize the command with a Runnable and optional subsystem requirements
    public HydraDirectCommand(Runnable toRun, HydraSubsystem... requirements) {
        this.toRun = toRun;
        addRequirements(requirements); // Add specified subsystem requirements
    }

    // Default constructor initializes the command with an empty Runnable
    public HydraDirectCommand() {
        toRun = () -> {}; // Empty Runnable
    }

    // Initializes the command by executing the provided Runnable
    @Override
    public void initialize() {
        toRun.run(); // Execute the Runnable
    }

    // Indicates that the command is always finished
    @Override
    public final boolean isFinished() {
        return true; // Direct commands are always considered finished after initialization
    }
}