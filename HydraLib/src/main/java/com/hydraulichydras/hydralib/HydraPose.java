package com.hydraulichydras.hydralib;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Locale;

/**
 *  Pose contains methods and attributes to set and retrieve position and orientation values
 *  allowing for precise control and monitoring of the robot's movement in a 3D space.
 */

public class HydraPose {
    public double x;
    public double y;
    public double heading;

    public HydraPose() {
        this(0.0, 0.0, 0.0);
    }
    public HydraPose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = AngleUnit.normalizeRadians(heading);
    }

    public HydraPose(HydraVector2d vector, double heading) {
        this(vector.x, vector.y, heading);
    }

    public void set(HydraPose pose) {
        this.x = pose.x;
        this.y = pose.y;
        this.heading = pose.heading;
    }

    public HydraPose(double x, double y) {
        this(x, y, 0.0);
    }

    public HydraPose add(HydraPose other) {
        return new HydraPose(x + other.x, y + other.y, heading + other.heading);
    }

    public HydraPose subtract(HydraPose other) {
        return new HydraPose(x - other.x, y - other.y,  AngleUnit.normalizeRadians(heading - other.heading));
    }

    public HydraPose divide(HydraPose other) {
        return new HydraPose(this.x / other.x, this.y / other.y, this.heading / other.heading);
    }

    public HydraPose subt(HydraPose other) {
        return new HydraPose(x - other.x, y - other.y, heading - other.heading);
    }

    public HydraVector2d toVector2d() {
        return new HydraVector2d(x, y);
    }

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

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f %.2f %.3f", x, y, heading);
    }
}