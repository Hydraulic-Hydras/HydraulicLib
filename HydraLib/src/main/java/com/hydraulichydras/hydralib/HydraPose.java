package com.hydraulichydras.hydralib;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Locale;

/**
 *  Pose contains methods and attributes to set and retrieve position and orientation values
 *  allowing for precise control and monitoring of the robot's movement in a 3D space.
 */

public class HydraPose {

    // Position coordinates
    public double x;
    public double y;
    public double heading; // Heading angle (in radians)

    // Default constructor initializes the pose to origin (0, 0) with zero heading
    public HydraPose() {
        this(0.0, 0.0, 0.0);
    }

    // Constructor to initialize pose with specified coordinates and heading
    public HydraPose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        // Normalize heading angle
        this.heading = AngleUnit.normalizeRadians(heading);
    }

    // Constructor to initialize pose with a vector and heading
    public HydraPose(HydraVector2d vector, double heading) {
        this(vector.x, vector.y, heading);
    }

    // Set the pose to the values of another pose
    public void set(HydraPose pose) {
        this.x = pose.x;
        this.y = pose.y;
        this.heading = pose.heading;
    }

    // Constructor to initialize pose with specified coordinates and zero heading
    public HydraPose(double x, double y) {
        this(x, y, 0.0);
    }

    // Method to add another pose to this pose
    public HydraPose add(HydraPose other) {
        return new HydraPose(x + other.x, y + other.y, heading + other.heading);
    }

    // Method to subtract another pose from this pose
    public HydraPose subtract(HydraPose other) {
        return new HydraPose(x - other.x, y - other.y,  AngleUnit.normalizeRadians(heading - other.heading));
    }

    // Method to divide this pose by another pose
    public HydraPose divide(HydraPose other) {
        return new HydraPose(this.x / other.x, this.y / other.y, this.heading / other.heading);
    }

    // Method to compute the difference between this pose and another pose
    public HydraPose subt(HydraPose other) {
        return new HydraPose(x - other.x, y - other.y, heading - other.heading);
    }

    // Convert the pose to a 2D vector
    public HydraVector2d toVector2d() {
        return new HydraVector2d(x, y);
    }

    public HydraVector2d vec() {
        return new HydraVector2d(x, y);
    }

    public HydraVector2d headingVec() {
        return new HydraVector2d(Math.cos(heading), Math.sin(heading));
    }

    // Override equals method to compare poses for equality
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof HydraPose) {
            HydraPose pose = (HydraPose) obj;
            return (x == pose.x) && (y == pose.y) && (heading == pose.heading);
        }
        return false;
    }

    public boolean epsilonEquals(HydraPose other) {
        return epsilonEquals(x, other.x) && epsilonEquals(y, other.y) && epsilonEquals(heading, other.heading);
    }

    private static boolean epsilonEquals(double a, double b) {
        return Math.abs(a - b) < Math.pow(10, -10);
    }

    public boolean epsilonEqualsHeading(HydraPose other) {
        return epsilonEquals(x, other.x) && epsilonEquals(y, other.y) && HydraAngle.normDelta(heading - other.heading) == 0.0;
    }

    // Getter method for X coordinate
    public double getX() {
        return x;
    }

    // Getter method for Y coordinate
    public double getY() {
        return y;
    }

    // Getter method for heading
    public double getHeading() {
        return heading;
    }

    // Override toString method to provide a string representation of the pose
    @Override
    public String toString() {
        // Format the pose as a string with two decimal places for position coordinates and three decimal places for heading
        return String.format(Locale.ENGLISH, "%.2f %.2f %.3f", x, y, heading);
    }
}