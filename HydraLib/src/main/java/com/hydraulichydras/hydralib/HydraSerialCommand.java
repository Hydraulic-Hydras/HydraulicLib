package com.hydraulichydras.hydralib;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a serial command group where commands are executed sequentially.
 */
public class HydraSerialCommand extends HydraCommandGroupedFoundation {

    // List to store the serially executed commands
    private final List<HydraCommand> S_commands = new ArrayList<>();

    // Index of the current command being executed
    private int currentCommandIndex = 1;

    // Flag indicating if the command group should run when disabled
    private boolean runWhenDisabled = true;

    // Constructor to initialize the serial command group with specified commands
    public HydraSerialCommand(HydraCommand... commands) {
        addCommands(commands);
    }

    // Adds commands to the serial command group
    @Override
    public void addCommands(HydraCommand... commands) {
        requireUnGrouped(commands);

        // Check if commands can be added while the group is running
        if (currentCommandIndex != 1) {
            throw new IllegalStateException(
                    "Commands cannot be added to a Machine while the group is running");
        }

        // Register the grouped commands
        registerGroupedCommands(commands);

        // Add commands to the list and update requirements and runWhenDisabled flag
        for (HydraCommand command : commands) {
            S_commands.add(command);
            requirements.addAll(command.getRequirements());
            runWhenDisabled &= command.runsWhenDisabled();
        }
    }

    // Initializes the serial command group
    @Override
    public void initialize() {
        currentCommandIndex = 0;

        // Initialize the first command if the list is not empty
        if (!S_commands.isEmpty()) {
            S_commands.get(0).initialize();
        }
    }

    // Executes the serial command group
    @Override
    public void execute() {
        // Return if the command list is empty
        if (S_commands.isEmpty()) {
            return;
        }

        // Get the current command
        HydraCommand currentCommand = S_commands.get(currentCommandIndex);

        // Execute the current command and proceed to the next if finished
        currentCommand.execute();
        if (currentCommand.isFinished()) {
            currentCommand.end(false);
            currentCommandIndex++;
            // Initialize the next command if not the last
            if (currentCommandIndex < S_commands.size()) {
                S_commands.get(currentCommandIndex).initialize();
            }
        }
    }

    // Ends the serial command group
    @Override
    public void end(boolean interrupted) {
        // End the current command if interrupted
        if (interrupted && !S_commands.isEmpty()) {
            S_commands.get(currentCommandIndex).end(true);
        }
        currentCommandIndex = -1; // Reset the current command index
    }

    // Checks if the serial command group is finished
    @Override
    public boolean isFinished() {
        return currentCommandIndex == S_commands.size();
    }

    // Indicates if the serial command group should run when disabled
    @Override
    public boolean runsWhenDisabled() {
        return runWhenDisabled;
    }
}