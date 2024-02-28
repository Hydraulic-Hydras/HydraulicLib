package com.hydraulichydras.hydralib;

/**
 * Interface for controlling the drivetrain of a robotic platform.
 * Implementations translate poses into motor commands for movement.
 */
public interface HydraDrivetrain {
    void set(HydraPose pose);
}
