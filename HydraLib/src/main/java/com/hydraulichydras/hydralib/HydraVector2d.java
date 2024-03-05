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

    public static HydraVector2d polar(double r, double theta) {
        return new HydraVector2d(r * Math.cos(theta), r * Math.sin(theta));
    }

    public double norm() {
        return Math.hypot(x, y);
    }

    public double Angle() {
        return HydraAngle.norm(Math.atan2(y, x));
    }

    public double angleBetween(HydraVector2d other) {
        return Math.acos((this.dot(other)) / (this.norm() * other.norm()));
    }
    public double dot(HydraVector2d other) {
        return x * other.x + y * other.y;
    }

    // Method to multiply the vector by a scalar
    public HydraVector2d mult(double scalar) {
        return new HydraVector2d(x * scalar, y * scalar);
    }

    // Method to divide the vector by a scalar
    public HydraVector2d divide(double scalar) {
        if (scalar == 0) throw new IllegalArgumentException("Division by zero");
        return new HydraVector2d(x / scalar, y / scalar);
    }

    // Method to subtract another vector from this vector
    public HydraVector2d subt(HydraVector2d other) {
        return new HydraVector2d(x - other.x, y - other.y);
    }

    public HydraVector2d plus(HydraVector2d other) {
        return new HydraVector2d(x + other.x, y + other.y);
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

    // Getter method for X coordinate
    public double getX() {
        return x;
    }

    // Getter method for Y coordinate
    public double getY() {
        return y;
    }

    public boolean epsilonEquals(HydraVector2d other) {
        return Double.compare(x, other.x) == 0 && Double.compare(y, other.y) == 0;
    }

    // Override toString method to provide a string representation of the vector
    @Override
    public String toString() {
        // Format the vector as a string with two decimal places for components
        return String.format("{%.2f, %.2f}", x, y);
    }
}