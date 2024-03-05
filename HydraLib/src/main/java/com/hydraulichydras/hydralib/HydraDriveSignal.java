package com.hydraulichydras.hydralib;

/**
 * Signal indicating the commanded kinematic state of a drive.
 */
public class HydraDriveSignal {

    private HydraPose vel;
    private HydraPose accel;

    /**
     * Constructs a DriveSignal with default values.
     */
    public HydraDriveSignal() {
        this.vel = new HydraPose();
        this.accel = new HydraPose();
    }

    /**
     * Constructs a DriveSignal with specified velocity and acceleration.
     * @param vel robot frame velocity
     * @param accel robot frame acceleration
     */
    public HydraDriveSignal(HydraPose vel, HydraPose accel) {
        this.vel = vel;
        this.accel = accel;
    }

    /**
     * Get the robot frame velocity.
     * @return the velocity
     */
    public HydraPose getVel() {
        return vel;
    }

    /**
     * Set the robot frame velocity.
     * @param vel the velocity to set
     */
    public void setVel(HydraPose vel) {
        this.vel = vel;
    }

    /**
     * Get the robot frame acceleration.
     * @return the acceleration
     */
    public HydraPose getAccel() {
        return accel;
    }

    /**
     * Set the robot frame acceleration.
     * @param accel the acceleration to set
     */
    public void setAccel(HydraPose accel) {
        this.accel = accel;
    }
}
