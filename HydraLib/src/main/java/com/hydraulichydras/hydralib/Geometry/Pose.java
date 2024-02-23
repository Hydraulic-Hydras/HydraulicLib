package com.hydraulichydras.hydralib.Geometry;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Locale;
/**
 *  Pose contains methods and attributes to set and retrieve position and orientation values
 *  allowing for precise control and monitoring of the robot's movement in a 3D space.
 *
 *  The Pose class inherits properties and behaviors from the more general Point class.
 */
public class Pose extends Point {

    // Math.toRadians() for heading
    public double heading;

    public Pose(double x, double y, double heading) {
        super(x, y);
        this.heading = AngleUnit.normalizeRadians(heading);
    }
    public Pose(Point p, double heading) {
        this(p.x, p.y, heading);
    }

    public Pose(Vector2D vec, double heading) {
        this(vec.x, vec.y, heading);
    }

    public Pose(){
        this(0, 0, 0);
    }

    public Pose add(Pose other) {
        return new Pose(x + other.x, y + other.y, heading + other.heading);
    }

    public Pose subtract(Pose other) {
        return new Pose(this.x - other.x, this.y - other.y, AngleUnit.normalizeRadians(this.heading - other.heading));
    }

    public Pose divide(Pose other) {
        return new Pose(this.x / other.x, this.y / other.y, this.heading / other.heading);
    }

    public Pose subt(Pose other) {
        return new Pose(x - other.x, y - other.y, heading - other.heading);
    }

    public Vector2D toVec2D() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f %.2f %.2f", x, y, heading);
    }
}