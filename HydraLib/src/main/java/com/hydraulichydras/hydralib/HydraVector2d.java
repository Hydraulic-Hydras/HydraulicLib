package com.hydraulichydras.hydralib;

/**
 *  A vector in 2D space typically consists of two components: one along the x-axis and one along the y-axis.
 *  This class is often used for geometric operations necessary.
 */
public class HydraVector2d {

    // Components of the vector
    public double x;
    public double y;

    // Default constructor initializes the vector to the origin (0, 0)
    public HydraVector2d() {
        this(0.0, 0.0);
    }

    // Constructor to initialize the vector with specified x and y components
    public HydraVector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Method to multiply the vector by a scalar
    public HydraVector2d mult(double scalar) {
        return new HydraVector2d(x * scalar, y * scalar);
    }

    // Method to divide the vector by a scalar
    public HydraVector2d divide(double scalar) {
        return new HydraVector2d(x / scalar, y / scalar);
    }

    // Method to subtract another vector from this vector
    public HydraVector2d subt(HydraVector2d other) {
        return new HydraVector2d(x - other.x, y - other.y);
    }

    // Method to compute the magnitude (length) of the vector
    public double magnitude() {
        return Math.hypot(x, y);
    }

    // Method to compute the unit vector (vector with magnitude 1) in the same direction as this vector
    public HydraVector2d unit() {
        return this.divide(magnitude());
    }

    // Method to compute the angle (in radians) of the vector with respect to the positive x-axis
    public double angle() {
        return HydraAngle.norm(Math.atan2(y, x));
    }

    // Method to rotate the vector by a specified angle (in radians) counter-clockwise
    public HydraVector2d rotation(double angle) {
        return new HydraVector2d(
                x * Math.cos(angle) - y * Math.sin(angle),
                x * Math.sin(angle) + y * Math.cos(angle)
        );
    }

    // Method to project this vector onto another vector
    public HydraVector2d project(HydraVector2d other) {
        double magnitude = other.magnitude();
        double angle = angle();
        return new HydraVector2d(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    // Override toString method to provide a string representation of the vector
    @Override
    public String toString() {
        // Format the vector as a string with two decimal places for components
        return String.format("{%.2f, %.2f}", x, y);
    }
}