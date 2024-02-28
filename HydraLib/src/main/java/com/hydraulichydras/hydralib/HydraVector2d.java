package com.hydraulichydras.hydralib;

/**
 *  A vector in 2D space typically consists of two components: one along the x-axis and one along the y-axis.
 *  This class is often used for geometric operations necessary.
 */
public class HydraVector2d {

    public double x;
    public double y;
    public HydraVector2d() {
        this(0.0, 0.0);
    }

    public HydraVector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public HydraVector2d mult(double scalar) {
        return new HydraVector2d(x * scalar, y * scalar);
    }

    public HydraVector2d divide(double scalar) {
        return new HydraVector2d(x / scalar, y / scalar);
    }

    public HydraVector2d subt(HydraVector2d other) {
        return new HydraVector2d(x - other.x, y - other.y);
    }

    public double magnitude() {
        return Math.hypot(x, y);
    }

    public HydraVector2d unit() {
        return this.divide(magnitude());
    }

    public double angle() {
        return HydraAngle.norm(Math.atan2(y, x));
    }

    public HydraVector2d rotation(double angle) {
        return new HydraVector2d(
                x * Math.cos(angle) - y * Math.sin(angle),
                x * Math.sin(angle) + y * Math.cos(angle)
        );
    }

    public HydraVector2d project(HydraVector2d other) {
        double magnitude = other.magnitude();
        double angle = angle();
        return new HydraVector2d(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    @Override
    public String toString() {
        return String.format("{%.2f, %.2f}", x, y);
    }
}