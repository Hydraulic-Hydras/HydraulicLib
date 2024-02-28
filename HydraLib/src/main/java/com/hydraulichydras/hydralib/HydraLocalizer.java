package com.hydraulichydras.hydralib;

/**
 * This interface, outlines the functionality required for an object responsible for localization within a system.
 */
public interface HydraLocalizer {

    /**
     *  Current robot pose estimate.
     */
    HydraPose getPoseEstimate();

    /**
     * Sets pose estimate
     */
    void setPoseEstimate(HydraPose pose);

    /**
     * Updates localization
     */
    void update();

}
