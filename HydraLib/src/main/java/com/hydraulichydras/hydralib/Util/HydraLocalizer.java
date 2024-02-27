package com.hydraulichydras.hydralib.Util;

import com.hydraulichydras.hydralib.Geometry.Pose;

/**
 * This interface, outlines the functionality required for an object responsible for localization within a system.
 */
public interface HydraLocalizer {
    /**
     * This method indicates that the localizer performs periodic actions or updates.
     */
    void periodic();

    /**
     * Returns the current pose
     */
    Pose getPos();

    /**
     * Sets the pose
     * <p>
     * @param pose
     */
    void setPos(Pose pose);
}
