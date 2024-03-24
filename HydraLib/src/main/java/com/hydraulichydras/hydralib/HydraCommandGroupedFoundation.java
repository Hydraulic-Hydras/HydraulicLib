package com.hydraulichydras.hydralib;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A foundation class for grouped commands that share common requirements.
 */
public abstract class HydraCommandGroupedFoundation extends HydraCommandFoundation implements HydraCommand {

    // Set to store the grouped commands
    private static final Set<HydraCommand> groupedCommands = Collections.newSetFromMap(new WeakHashMap<>());

    /**
     * Registers grouped commands.
     *
     * @param commands the commands to be registered as grouped commands
     */
    public static void registerGroupedCommands(HydraCommand... commands) {
        groupedCommands.addAll(Arrays.asList(commands));
    }

    /**
     * Clears all grouped commands.
     */
    public static void clearGroupedCommands() {
        groupedCommands.clear();
    }

    /**
     * Clears a specific grouped command.
     *
     * @param command the command to be removed from grouped commands
     */
    public static void clearGroupedCommand(HydraCommand command) {
        groupedCommands.remove(command);
    }

    /**
     * Checks if commands are ungrouped.
     *
     * @param commands the commands to check
     * @throws IllegalArgumentException if commands are added to more than one CommandGroup
     */
    public static void requireUnGrouped(Collection<HydraCommand> commands) {
        if (!Collections.disjoint(commands, getGroupedCommands())) {
            throw new IllegalArgumentException("Commands cannot be added to more than one CommandGroup");
        }
    }

    /**
     * Checks if commands are ungrouped.
     *
     * @param commands the commands to check
     */
    public static void requireUnGrouped(HydraCommand... commands) {
        requireUnGrouped(Arrays.asList(commands));
    }

    /**
     * Retrieves the grouped commands.
     *
     * @return the set of grouped commands
     */
    public static Set<HydraCommand> getGroupedCommands() {
        return groupedCommands;
    }

    /**
     * Adds commands to the group.
     *
     * @param commands the commands to be added to the group
     */
    public abstract void addCommands(HydraCommand... commands);
}
