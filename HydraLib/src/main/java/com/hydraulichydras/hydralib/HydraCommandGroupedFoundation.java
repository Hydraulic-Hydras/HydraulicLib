package com.hydraulichydras.hydralib;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class HydraCommandGroupedFoundation extends HydraCommandFoundation implements HydraCommand {

    private static final Set<HydraCommand> groupedCommands = Collections.newSetFromMap(new WeakHashMap<>());
    public static void registerGroupedCommands(HydraCommand... commands) {
        groupedCommands.addAll(Arrays.asList(commands));
    }

    public static void clearGroupedCommands() {
        groupedCommands.clear();
    }

    public static void clearGroupedCommand(HydraCommand command) {
        groupedCommands.remove(command);
    }

    public static void requireUnGrouped(Collection<HydraCommand> commands) {
        if (!Collections.disjoint(commands, getGroupedCommands())) {
            throw new IllegalArgumentException("Commands cannot be added to more than one CommandGroup");
        }
    }
    public static void requireUnGrouped(HydraCommand... commands) {
        requireUnGrouped(Arrays.asList(commands));
    }


    public static Set<HydraCommand> getGroupedCommands() {
        return groupedCommands;
    }

    public abstract void addCommands(HydraCommand... commands);
}
