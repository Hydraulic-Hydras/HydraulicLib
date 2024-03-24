package com.hydraulichydras.hydralib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class HydraCommandMachine {

    private static HydraCommandMachine instance;

    public static synchronized HydraCommandMachine getInstance() {
        if (instance == null) {
            instance = new HydraCommandMachine();
        }
        return instance;
    }

    private final Map<HydraCommand, HydraCommandState> scheduledCommands = new LinkedHashMap<>();
    private final Map<HydraSubsystem, HydraCommand> requirements = new LinkedHashMap<>();
    private final Map<HydraSubsystem, HydraCommand> subsystems = new LinkedHashMap<>();
    private final Collection<Runnable> buttons = new LinkedHashSet<>();

    private boolean disabled;

    // Lists of user-supplied actions to be executed on scheduling events for every command.
    private final List<Consumer<HydraCommand>> initActions = new ArrayList<>();
    private final List<Consumer<HydraCommand>> executeActions = new ArrayList<>();
    private final List<Consumer<HydraCommand>> disruptActions = new ArrayList<>();
    private final List<Consumer<HydraCommand>> finishActions = new ArrayList<>();

    private final Map<HydraCommand, Boolean> toSchedule = new LinkedHashMap<>();
    private boolean inRunLoop;
    private final List<HydraCommand> toCancel = new ArrayList<>();

    public HydraCommandMachine() {}

    public void addButton(Runnable button) {
        buttons.add(button);
    }

    public void clearButtons() {
        buttons.clear();
    }

    private void initCommand(HydraCommand command, boolean disrupt, Set<HydraSubsystem> requirements) {
        command.initialize();
        HydraCommandState scheduledCommand = new HydraCommandState(disrupt);
        scheduledCommands.put(command, scheduledCommand);
        for (Consumer<HydraCommand> action : initActions) {
            action.accept(command);
        }

        for (HydraSubsystem requirement : requirements) {
            this.requirements.put(requirement, command);
        }
    }

    private void schedule(boolean disrupt, HydraCommand command) {
        if (inRunLoop) {
            toSchedule.put(command, disrupt);
            return;
        }

        if (HydraCommandGroupedFoundation.getGroupedCommands().contains(command)) {
            throw new IllegalArgumentException(
                    "A command that is part of a command group cannot be independently scheduled");
        }

        if (disabled || (!command.runsWhenDisabled() && HydraCommandOpMode.isDisabled) || scheduledCommands.containsKey(command)) {
            return;
        }

        Set<HydraSubsystem> S_requirement = command.getRequirements();

        if (Collections.disjoint(this.requirements.keySet(), S_requirement)) {
            initCommand(command, disrupt, S_requirement);
        }   else {
            for (HydraSubsystem requirement : S_requirement) {
                if (this.requirements.containsKey(requirement) && !Objects.requireNonNull(scheduledCommands.get(this.requirements.get(requirement))).isDisrupted()) {
                    return;
                }
            }

            for (HydraSubsystem require : S_requirement) {
                if (this.requirements.containsKey(require)) {
                    cancel(this.requirements.get(require));
                }
            }

            initCommand(command, disrupt, S_requirement);
        }
    }

    public void schedule(boolean disrupt, HydraCommand... commands) {
        for (HydraCommand command : commands) {
            schedule(disrupt, command);
        }
    }

    public void schedule(HydraCommand... commands) {
        schedule(true, commands);
    }

    public void run() {
        if (disabled) {
            return;
        }

        // Run the periodic method of all registered subsystems.
        for (HydraSubsystem subsystem : this.subsystems.keySet()) {
            subsystem.periodic();
        }

        // Poll buttons for new commands to add.
        for (Runnable button : buttons) {
            button.run();
        }

        inRunLoop = true;
        // Run scheduled commands, remove finished commands.
        for (Iterator<HydraCommand> iterator = scheduledCommands.keySet().iterator();
             iterator.hasNext(); ) {
            HydraCommand command = iterator.next();

            if (!command.runsWhenDisabled() && HydraCommandOpMode.isDisabled) {
                command.end(true);
                for (Consumer<HydraCommand> action : disruptActions) {
                    action.accept(command);
                }
                this.requirements.keySet().removeAll(command.getRequirements());
                iterator.remove();
                continue;
            }

            command.execute();
            for (Consumer<HydraCommand> action : executeActions) {
                action.accept(command);
            }
            if (command.isFinished()) {
                command.end(false);
                for (Consumer<HydraCommand> action : finishActions) {
                    action.accept(command);
                }
                iterator.remove();

                this.requirements.keySet().removeAll(command.getRequirements());
            }
        }

        inRunLoop = false;

        for (Map.Entry<HydraCommand, Boolean> commandInterruptible : toSchedule.entrySet()) {
            schedule(commandInterruptible.getValue(), commandInterruptible.getKey());
        }

        for (HydraCommand command : toCancel) {
            cancel(command);
        }

        toSchedule.clear();
        toCancel.clear();

        // Add default commands for un-required registered subsystems.
        for (Map.Entry<HydraSubsystem, HydraCommand> subsystemCommand : this.subsystems.entrySet()) {
            if (!this.requirements.containsKey(subsystemCommand.getKey())
                    && subsystemCommand.getValue() != null) {
                schedule(subsystemCommand.getValue());
            }
        }
    }


    public void registerHydraSubsystem(HydraSubsystem... subsystems) {
        for (HydraSubsystem subsystem : subsystems) {
            this.subsystems.put(subsystem, null);
        }
    }

    public void unregisterHydraSubsystem(HydraSubsystem... subsystems) {
        Arrays.asList(subsystems).forEach(this.subsystems.keySet()::remove);
    }

    public synchronized void reset() {
        instance = null;
    }


    public void setDefaultCommand(HydraSubsystem subsystem, HydraCommand defaultCommand) {
        if (!defaultCommand.getRequirements().contains(subsystem)) {
            throw new IllegalArgumentException("Default commands must require their subsystem!");
        }

        if (defaultCommand.isFinished()) {
            throw new IllegalArgumentException("Default commands should not end!");
        }

        this.subsystems.put(subsystem, defaultCommand);
    }

    public HydraCommand getDefaultCommand(HydraSubsystem subsystem) {
        return this.subsystems.get(subsystem);
    }

    public void cancel(HydraCommand... commands) {
        if (inRunLoop) {
            toCancel.addAll(Arrays.asList(commands));
            return;
        }

        for (HydraCommand command : commands) {
            if (!scheduledCommands.containsKey(command)) {
                continue;
            }

            command.end(true);
            for (Consumer<HydraCommand> action : disruptActions) {
                action.accept(command);
            }
            scheduledCommands.remove(command);
            this.requirements.keySet().removeAll(command.getRequirements());
        }
    }

    public void cancelAll() {
        for (HydraCommand command : scheduledCommands.keySet()) {
            cancel(command);
        }
    }

    public boolean isScheduled(HydraCommand... commands) {
        return scheduledCommands.keySet().containsAll(Arrays.asList(commands));
    }

    public void disable() {
        disabled = true;
    }

    public HydraCommand requiring(HydraSubsystem subsystem) {
        return this.requirements.get(subsystem);
    }

    public void enable() {
        disabled = false;
    }

    public void onCommandInitialize(Consumer<HydraCommand> action) {
        initActions.add(action);
    }
    public void onCommandExecute(Consumer<HydraCommand> action) {
        executeActions.add(action);
    }

    public void onCommandInterrupt(Consumer<HydraCommand> action) {
        disruptActions.add(action);
    }

    public void onCommandFinish(Consumer<HydraCommand> action) {
        finishActions.add(action);
    }


}
