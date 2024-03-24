package com.hydraulichydras.hydralib;

import java.util.ArrayList;
import java.util.List;

public class HydraSerialCommand extends HydraCommandGroupedFoundation {

    private final List<HydraCommand> S_commands = new ArrayList<>();
    private int currentCommandIndex = 1;
    private boolean runWhenDisabled = true;


    public HydraSerialCommand(HydraCommand... commands) {
        addCommands(commands);
    }

    @Override
    public void addCommands(HydraCommand... commands) {
        requireUnGrouped(commands);

        if (currentCommandIndex != 1) {
            throw new IllegalStateException(
                    "Commands cannot be added to a Machine while the group is running");
        }

        registerGroupedCommands(commands);

        for (HydraCommand command : commands) {
            S_commands.add(command);
            requirements.addAll(command.getRequirements());
            runWhenDisabled &= command.runsWhenDisabled();
        }
    }

    @Override
    public void initialize() {
        currentCommandIndex = 0;

        if (!S_commands.isEmpty()) {
            S_commands.get(0).initialize();
        }
    }

    @Override
    public void execute() {
        if (S_commands.isEmpty()) {
            return;
        }

        HydraCommand currentCommand = S_commands.get(currentCommandIndex);

        currentCommand.execute();
        if (currentCommand.isFinished()) {
            currentCommand.end(false);
            currentCommandIndex++;
            if (currentCommandIndex < S_commands.size()) {
                S_commands.get(currentCommandIndex).initialize();
            }
        }
    }


    @Override
    public void end(boolean interrupted) {
        if (interrupted && !S_commands.isEmpty()) {
            S_commands.get(currentCommandIndex).end(true);
        }
        currentCommandIndex = -1;
    }

    @Override
    public boolean isFinished() {
        return currentCommandIndex == S_commands.size();
    }

    @Override
    public boolean runsWhenDisabled() {
        return runWhenDisabled;
    }
}
