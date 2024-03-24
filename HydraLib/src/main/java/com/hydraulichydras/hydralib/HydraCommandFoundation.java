package com.hydraulichydras.hydralib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class HydraCommandFoundation implements HydraCommand {

    protected String name = this.getClass().getSimpleName();
    protected String subsystem = "Not set";
    protected Set<HydraSubsystem> requirements = new HashSet<>();

    public final void addRequirements(HydraSubsystem... requirement) {
        requirements.addAll(Arrays.asList(requirement));
    }

    @Override
    public Set<HydraSubsystem> getRequirements() {
        return requirements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }
}
