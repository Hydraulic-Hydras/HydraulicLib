package com.hydraulichydras.hydralib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A foundation class for implementing HydraCommand interface.
 * Provides basic functionality and fields common to most commands.
 */
public abstract class HydraCommandFoundation implements HydraCommand {

    // Default name of the command
    protected String name = this.getClass().getSimpleName();

    // Default subsystem associated with the command
    protected String subsystem = "Not set";

    // Set to store the requirements (subsystems) needed by the command
    protected Set<HydraSubsystem> requirements = new HashSet<>();

    /**
     * Adds requirements (subsystems) needed by the command.
     *
     * @param requirement the subsystems to be added as requirements
     */
    public final void addRequirements(HydraSubsystem... requirement) {
        requirements.addAll(Arrays.asList(requirement));
    }

    /**
     * Retrieves the set of requirements (subsystems) needed by the command.
     *
     * @return the set of requirements needed by the command
     */
    @Override
    public Set<HydraSubsystem> getRequirements() {
        return requirements;
    }

    /**
     * Retrieves the name of the command.
     *
     * @return the name of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the command.
     *
     * @param name the name to set for the command
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the subsystem associated with the command.
     *
     * @return the subsystem associated with the command
     */
    public String getSubsystem() {
        return subsystem;
    }

    /**
     * Sets the subsystem associated with the command.
     *
     * @param subsystem the subsystem to set for the command
     */
    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }
}
