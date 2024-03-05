package com.hydraulichydras.hydralib;

/**
 * Abstraction for generic robot drive motion and localization. Robot poses are specified in a coordinate system with
 * positive x pointing forward, positive y pointing left, and positive heading measured counter-clockwise from the
 * x-axis.
 */
public abstract class HydraDrive {

    /**
     * Localizer used to determine the evolution of [poseEstimate].
     */
    protected HydraLocalizer localizer;

    private double headingOffset = 0.0;

    /**
     * The raw heading used for computing [externalHeading]. Not affected by [externalHeading] setter.
     */
    protected abstract double getRawExternalHeading();

    /**
     * The robot's heading in radians as measured by an external sensor (e.g., IMU, gyroscope).
     */
    public double getExternalHeading() {
        return HydraAngle.norm(getRawExternalHeading() + headingOffset);
    }

    public void setExternalHeading(double value) {
        headingOffset = -getRawExternalHeading() + value;
    }

    /**
     * The robot's current pose estimate.
     */
    public HydraPose getPoseEstimate() {
        return localizer.getPoseEstimate();
    }

    public void setPoseEstimate(HydraPose value) {
        localizer.setPoseEstimate(value);
    }

    /**
     *  Current robot pose velocity (optional)
     */
    public HydraPose getPoseVelocity() {
        return localizer.getPoseVelocity();
    }

    /**
     * Updates [poseEstimate] with the most recent positional change.
     */
    public void updatePoseEstimate() {
        localizer.update();
    }

    /**
     * Sets the current commanded drive state of the robot. Feedforward is applied to [driveSignal] before it reaches
     * the motors.
     */
    public abstract void setDriveSignal(HydraDriveSignal driveSignal);

    /**
     * Sets the current commanded drive state of the robot. Feedforward is *not* applied to [drivePower].
     */
    public abstract void setDrivePower(HydraPose drivePower);

    /**
     * The heading velocity used to determine pose velocity in some cases
     */
    public Double getExternalHeadingVelocity() {
        return null;
    }
}
