package com.hydraulichydras.hydralib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A command group that executes multiple commands in parallel.
 */
public class HydraCollateralCommand extends HydraSerialCommand {

    // Map to track the state of each command in the group
    private final Map<HydraCommand, Boolean> commands = new HashMap<>();

    // Flag indicating whether the group should run when the robot is disabled
    private boolean runWhenDisabled = true;

    /**
     * Constructs a new HydraCollateralCommand with the specified commands.
     *
     * @param commands the commands to be executed in parallel
     */
    public HydraCollateralCommand(HydraCommand... commands) {
        addCommands(commands);
    }

    /**
     * Adds commands to the group.
     *
     * @param commands the commands to be added to the group
     * @throws IllegalStateException if commands are added while the group is running
     * @throws IllegalArgumentException if multiple commands require the same subsystems
     */
    @Override
    public final void addCommands(HydraCommand... commands) {
        requireUnGrouped(commands);

        if (this.commands.containsValue(true)) {
            throw new IllegalStateException(
                    "Commands cannot be added to a CommandGroup while the group is running");
        }

        registerGroupedCommands(commands);

        for (HydraCommand command : commands) {
            if (!Collections.disjoint(command.getRequirements(), requirements)) {
                throw new IllegalArgumentException("Multiple commands in a parallel group cannot"
                        + "require the same subsystems");
            }
            this.commands.put(command, false);
            requirements.addAll(command.getRequirements());
            runWhenDisabled &= command.runsWhenDisabled();
        }
    }

    /**
     * Initializes all commands in the group.
     */
    @Override
    public void initialize() {
        for (Map.Entry<HydraCommand, Boolean> commandRunning : commands.entrySet()) {
            commandRunning.getKey().initialize();
            commandRunning.setValue(true);
        }
    }

    /**
     * Executes all commands in the group.
     */
    @Override
    public void execute() {
        for (Map.Entry<HydraCommand, Boolean> commandRunning : commands.entrySet()) {
            if (!commandRunning.getValue()) {
                continue;
            }
            commandRunning.getKey().execute();
            if (commandRunning.getKey().isFinished()) {
                commandRunning.getKey().end(false);
                commandRunning.setValue(false);
            }
        }
    }

    /**
     * Ends all commands in the group.
     *
     * @param interrupted true if the commands are interrupted, false otherwise
     */
    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            for (Map.Entry<HydraCommand, Boolean> commandRunning : commands.entrySet()) {
                if (commandRunning.getValue()) {
                    commandRunning.getKey().end(true);
                }
            }
        }
    }

    /**
     * Checks if all commands in the group have finished.
     *
     * @return true if all commands have finished, false otherwise
     */
    @Override
    public boolean isFinished() {
        return !commands.containsValue(true);
    }

    /**
     * Checks if the group should run when the robot is disabled.
     *
     * @return true if the group should run when disabled, false otherwise
     */
    @Override
    public boolean runsWhenDisabled() {
        return runWhenDisabled;
    }
}
