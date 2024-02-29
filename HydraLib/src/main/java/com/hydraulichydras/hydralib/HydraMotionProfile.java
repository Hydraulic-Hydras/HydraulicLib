package com.hydraulichydras.hydralib;

/**
 * Represents a motion profile for controlling motion with specified maximum velocity and acceleration.
 */
public class HydraMotionProfile {

    public double maxVel;
    public double maxAccel;
    public double distance;
    public double targetPosition;


    /**
     * Constructs a HydraMotionProfile with specified maximum velocity and acceleration.
     *
     * @param maxVel   Maximum velocity in units per second
     * @param maxAccel Maximum acceleration in units per second squared
     * @throws IllegalArgumentException if maxVel or maxAccel is not positive
     */
    public HydraMotionProfile(double maxVel, double maxAccel) {
        if (maxVel <= 0 || maxAccel <= 0) {
            throw new IllegalArgumentException("Max velocity and max acceleration must be positive.");
        }
        this.maxVel = maxVel;
        this.maxAccel = maxAccel;
    }

    /**
     * Calculates the motion profile based on initial and target positions.
     *
     * @param initialPos Initial position
     * @param TargetPos  Target position
     */
    public void calculateProfile(double initialPos, double TargetPos) {
        this.targetPosition = TargetPos;
        distance = Math.abs(TargetPos - initialPos);

        // Check if max acceleration is zero
        if (maxAccel == 0) {
            throw new IllegalStateException("Max acceleration cannot be zero.");
        }

        // Calculate acceleration time and distance
        double accelTime = maxVel / maxAccel;
        double accelDistance = 0.5 * maxAccel * accelTime * accelTime;

        // Adjust max velocity if there's not enough distance to reach it
        if (accelDistance >= distance / 2.0) {
            accelTime = Math.sqrt((2.0 * distance) / maxAccel);
            maxVel = maxAccel * accelTime;
        }
    }

    /**
     * Computes the output of the motion profile at a given time.
     *
     * @param time Time elapsed since start
     * @return Output of the motion profile at the given time
     * @throws IllegalStateException if motion profile is not calculated
     */
    public double getOutput(double time) {
        // Check if the motion profile has been calculated
        if (distance == 0) {
            throw new IllegalStateException("Motion profile not calculated.");
        }

        // Calculate output based on time
        if (time < 0) {
            return 0;
        } else if (time < (maxVel / maxAccel)) {
            // Acceleration phase
            return maxAccel * time;
        } else if (time < (distance / maxVel) + (maxVel / maxAccel)) {
            // Constant velocity phase
            return maxVel;
        } else if (time < (distance / maxVel) + (maxVel / maxAccel) * 2) {
            // Deceleration phase
            return maxAccel * ((distance / maxVel) + (maxVel / maxAccel) * 2 - time);
        } else {
            // Motion complete
            return 0;
        }
    }

    /**
     * Computes the total time for the motion profile.
     *
     * @return Total time for the motion profile
     */
    public double getTime() {
        // Calculate total time for the motion profile
        return (distance / maxVel) + (maxVel / maxAccel) * 2;
    }

    /**
     * Checks if the motion profile has finished at a given time.
     *
     * @param time Time elapsed since start
     * @return true if the motion profile has finished, otherwise false
     */
    public boolean isFinished(double time) {
        return time >= getTime();
    }

    /**
     * Returns the target position of the motion profile.
     *
     * @return The target position
     */
    public double getTargetPosition() {
        return targetPosition;
    }
}
