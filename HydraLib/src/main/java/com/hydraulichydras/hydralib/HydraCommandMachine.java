package com.hydraulichydras.hydralib;

import java.util.*;
import java.util.function.Consumer;

/**
 * Manages the execution and scheduling of Hydra commands.
 */
public final class HydraCommandMachine {

    // Singleton instance of the command machine
    private static HydraCommandMachine instance;

    // Returns the singleton instance of the command machine
    public static synchronized HydraCommandMachine getInstance() {
        if (instance == null) {
            instance = new HydraCommandMachine();
        }
        return instance;
    }

    // Map to store scheduled commands and their states
    private final Map<HydraCommand, HydraCommandState> scheduledCommands = new LinkedHashMap<>();

    // Map to store requirements (subsystems) and the commands associated with them
    private final Map<HydraSubsystem, HydraCommand> requirements = new LinkedHashMap<>();

    // Map to store registered subsystems and their default commands
    private final Map<HydraSubsystem, HydraCommand> subsystems = new LinkedHashMap<>();

    // Collection to store buttons that trigger commands
    private final Collection<Runnable> buttons = new LinkedHashSet<>();

    // Flag indicating if the robot is disabled
    private boolean disabled;

    // Lists of user-supplied actions to be executed on scheduling events for every command
    private final List<Consumer<HydraCommand>> initActions = new ArrayList<>();
    private final List<Consumer<HydraCommand>> executeActions = new ArrayList<>();
    private final List<Consumer<HydraCommand>> disruptActions = new ArrayList<>();
    private final List<Consumer<HydraCommand>> finishActions = new ArrayList<>();

    // Map to store commands to be scheduled and their interruptibility
    private final Map<HydraCommand, Boolean> toSchedule = new LinkedHashMap<>();

    // Flag indicating if the command machine is in a run loop
    private boolean inRunLoop;

    // List of commands to be canceled
    private final List<HydraCommand> toCancel = new ArrayList<>();

    // Private constructor to enforce singleton pattern
    private HydraCommandMachine() {
    }

    // Registers a button that triggers commands
    public void addButton(Runnable button) {
        buttons.add(button);
    }

    // Clears all registered buttons
    public void clearButtons() {
        buttons.clear();
    }

    // Initializes a command and adds it to the scheduled commands
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

    // Schedules a command with interruptibility and checks for conflicts
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
        } else {
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

    // Schedules multiple commands with interruptibility
    public void schedule(boolean disrupt, HydraCommand... commands) {
        for (HydraCommand command : commands) {
            schedule(disrupt, command);
        }
    }

    // Schedules multiple commands with interruptibility (default to interruptible)
    public void schedule(HydraCommand... commands) {
        schedule(true, commands);
    }

    // Runs the command machine
    public void run() {
        if (disabled) {
            return;
        }

        // Run the periodic method of all registered subsystems
        for (HydraSubsystem subsystem : this.subsystems.keySet()) {
            subsystem.periodic();
        }

        // Poll buttons for new commands to add
        for (Runnable button : buttons) {
            button.run();
        }

        inRunLoop = true;

        // Run scheduled commands, remove finished commands
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

        // Schedule commands and cancel commands based on queued actions
        for (Map.Entry<HydraCommand, Boolean> commandInterruptible : toSchedule.entrySet()) {
            schedule(commandInterruptible.getValue(), commandInterruptible.getKey());
        }

        for (HydraCommand command : toCancel) {
            cancel(command);
        }

        toSchedule.clear();
        toCancel.clear();

        // Add default commands for un-required registered subsystems
        for (Map.Entry<HydraSubsystem, HydraCommand> subsystemCommand : this.subsystems.entrySet()) {
            if (!this.requirements.containsKey(subsystemCommand.getKey())
                    && subsystemCommand.getValue() != null) {
                schedule(subsystemCommand.getValue());
            }
        }
    }

    // Registers Hydra subsystems
    public void registerHydraSubsystem(HydraSubsystem... subsystems) {
        for (HydraSubsystem subsystem : subsystems) {
            this.subsystems.put(subsystem, null);
        }
    }

    // Unregisters Hydra subsystems
    public void unregisterHydraSubsystem(HydraSubsystem... subsystems) {
        Arrays.asList(subsystems).forEach(this.subsystems.keySet()::remove);
    }

    // Resets the singleton instance of the command machine
    public synchronized void reset() {
        instance = null;
    }

    // Sets the default command for a subsystem
    public void setDefaultCommand(HydraSubsystem subsystem, HydraCommand defaultCommand) {
        // Check if the default command requires the subsystem
        if (!defaultCommand.getRequirements().contains(subsystem)) {
            throw new IllegalArgumentException("Default commands must require their subsystem!");
        }

        // Check if the default command is finished (it should not end)
        if (defaultCommand.isFinished()) {
            throw new IllegalArgumentException("Default commands should not end!");
        }

        // Set the default command for the subsystem
        this.subsystems.put(subsystem, defaultCommand);
    }

    // Retrieves the default command for a subsystem
    public HydraCommand getDefaultCommand(HydraSubsystem subsystem) {
        return this.subsystems.get(subsystem);
    }

    // Cancels specified commands
    public void cancel(HydraCommand... commands) {
        if (inRunLoop) {
            toCancel.addAll(Arrays.asList(commands));
            return;
        }

        // Iterate through specified commands
        for (HydraCommand command : commands) {
            // Check if the command is scheduled
            if (!scheduledCommands.containsKey(command)) {
                continue;
            }

            // End the command, perform disruption actions, and remove from scheduled commands
            command.end(true);
            for (Consumer<HydraCommand> action : disruptActions) {
                action.accept(command);
            }
            scheduledCommands.remove(command);
            this.requirements.keySet().removeAll(command.getRequirements());
        }
    }

    // Cancels all scheduled commands
    public void cancelAll() {
        for (HydraCommand command : scheduledCommands.keySet()) {
            cancel(command);
        }
    }

    // Checks if specified commands are scheduled
    public boolean isScheduled(HydraCommand... commands) {
        return scheduledCommands.keySet().containsAll(Arrays.asList(commands));
    }

    // Disables the command machine
    public void disable() {
        disabled = true;
    }

    // Retrieves the command requiring a specified subsystem
    public HydraCommand requiring(HydraSubsystem subsystem) {
        return this.requirements.get(subsystem);
    }

    // Enables the command machine
    public void enable() {
        disabled = false;
    }

    // Adds an action to execute when a command is initialized
    public void onCommandInitialize(Consumer<HydraCommand> action) {
        initActions.add(action);
    }

    // Adds an action to execute when a command is executed
    public void onCommandExecute(Consumer<HydraCommand> action) {
        executeActions.add(action);
    }

    // Adds an action to execute when a command is interrupted
    public void onCommandInterrupt(Consumer<HydraCommand> action) {
        disruptActions.add(action);
    }

    // Adds an action to execute when a command finishes
    public void onCommandFinish(Consumer<HydraCommand> action) {
        finishActions.add(action);
    }
}
