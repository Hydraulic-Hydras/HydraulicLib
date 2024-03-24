package com.hydraulichydras.hydralib;

import java.util.Arrays;
import java.util.Hashset;
import java.util.Set;

/*
 * Foundation class for the Command Machine.
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class HydraCommandFoundation implements HydraCommand {
    
    protected String name = this.getClass().getSimpleName();
    protected String subsystem = "Not set";
    protected Set<HydraSubsystem> requirements = new HashSet<>();

    
}
