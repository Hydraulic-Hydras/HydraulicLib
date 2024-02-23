package com.hydraulichydras.hydralib.Drive;

import com.hydraulichydras.hydralib.Geometry.Pose;

/**
 * This interface outlines a fundamental functionality related to controlling a drivetrain system
 */
public interface Drivetrain {
    /**
     * This method specifies a way to set the pose for your drivetrain system
     * @param pose
     */
    void set(Pose pose);
}
