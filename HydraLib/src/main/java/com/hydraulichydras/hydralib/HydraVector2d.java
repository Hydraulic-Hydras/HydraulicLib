package com.hydraulichydras.hydralib;

/**
 *  A vector in 2D space typically consists of two components: one along the x-axis and one along the y-axis.
 *  This class is often used for geometric operations necessary.
 */
public class HydraVector2d {

    // Components of the vector
    public double x;
    public double y;

    public final static HydraVector2d FORWARD = new HydraVector2d(0, 1);
    public final static HydraVector2d BACKWARD = new HydraVector2d(0, -1);
    public final static HydraVector2d LEFT = new HydraVector2d(-1, 0);
    public final static HydraVector2d RIGHT = new HydraVector2d(1, 1);
    public final static HydraVector2d ZERO = new HydraVector2d(0, 0);


    // Default constructor initializes the vector to the origin (0, 0)
    public HydraVector2d() {
        this(0.0, 0.0);
    }

    // Constructor to initialize the vector with specified x and y components
    public HydraVector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //makes a unit vector with a certain angle
    public HydraVector2d(HydraAngle angle) {
        this.x = Math.cos(Math.toRadians(angle.convertAngle(HydraAngle.AngleType.NEG_180_TO_180_CARTESIAN).getAngle()));
        this.y = Math.sin(Math.toRadians(angle.convertAngle(HydraAngle.AngleType.NEG_180_TO_180_CARTESIAN).getAngle()));
        this.fixFloatingPointErrors();
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

    // Method to multiply a vector by another vector
    public double dot(HydraVector2d other) {
        return getX() * other.getX() + getY() * other.getY();
    }

    // Method to multiply the vector by a scalar
    public HydraVector2d mult(double scalar) {
        return new HydraVector2d(x * scalar, y * scalar);
    }

    public HydraVector2d scale(double scale) {
        return new HydraVector2d(getX() * scale, getY() * scale);
    }


    // Method to divide the vector by a scalar
    public HydraVector2d divide(double scalar) {
        if (scalar == 0) throw new IllegalArgumentException("Division by zero");
        return new HydraVector2d(x / scalar, y / scalar);
    }

    // Method to subtract another vector from this vector
    public HydraVector2d subt(HydraVector2d other) {
        return new HydraVector2d(x - other.getX(), y - other.getY());
    }

    // Method to add another vector from this vector
    public HydraVector2d plus(HydraVector2d other) {
        return new HydraVector2d(x + other.getX(), y + other.getY());
    }

    // Method to compute the magnitude (length) of the vector
    public double magnitude() {
        return Math.hypot(x, y);
    }

    // Method using the direct application of the Pythagorean theorem.
    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Method to compute the unit vector (vector with magnitude 1) in the same direction as this vector
    public HydraVector2d unit() {
        return this.divide(magnitude());
    }

    // Method to compute the angle (in radians) of the vector with respect to the positive x-axis
    public double angle() {
        return HydraAngle.norm(Math.atan2(y, x));
    }

    //returns Angle object
    public HydraAngle getAngle() {
        double angRad = Math.atan2(y, x);
        return new HydraAngle(Math.toDegrees(angRad), HydraAngle.AngleType.NEG_180_TO_180_CARTESIAN);
    }

    //returns numerical value for angle in specified type
    public double getAngleDouble(HydraAngle.AngleType type) {
        return this.getAngle().convertAngle(type).getAngle();
    }

    // Method to rotate the vector by a specified angle (in radians) counter-clockwise
    public HydraVector2d rotation(double angle) {
        return new HydraVector2d(
                x * Math.cos(angle) - y * Math.sin(angle),
                x * Math.sin(angle) + y * Math.cos(angle)
        );
    }

    //returns HydraVector2d with the same magnitude as this but at the same angle as an Angle object
    public HydraVector2d rotateTo (HydraAngle ang) {
        return new HydraVector2d(ang).scale(this.getMagnitude());
    }



    // returns HydraVector2d rotated by ang degrees
    public HydraVector2d rotateBy(double ang, HydraAngle.Direction direction) {
        double angRads;
        if (direction == HydraAngle.Direction.COUNTER_CLOCKWISE) {
            angRads = Math.toRadians(ang); //default vector rotation direction is CCW
        } else {
            angRads = -1 * Math.toRadians(ang);
        }
        return new HydraVector2d(x * Math.cos(angRads) - y * Math.sin(angRads), x * Math.sin(angRads) + y * Math.cos(angRads));
    }

    // Method to project this vector onto another vector
    public HydraVector2d project(HydraVector2d other) {
        double magnitude = other.magnitude();
        double angle = angle();
        return new HydraVector2d(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    //projection of current vector onto v
    public HydraVector2d projection (HydraVector2d v) {
        return v.scale(dot(v)/(Math.pow(v.getMagnitude(), 2))); // u dot v over mag(v)^2 times v
    }

    public void fixFloatingPointErrors() {
        if (Math.abs(this.x) < 1e-5) {
            this.x = 0;
        }
        if (Math.abs(this.y) < 1e-5) {
            this.y = 0;
        }
    }

    // Getter method for X coordinate
    public double getX() {
        return x;
    }

    // Getter method for Y coordinate
    public double getY() {
        return y;
    }

    // Setter method for X coordinate
    public double setX(double x) { 
        return this.x = x;
    }

    // Setter method for Y coordinate
    public double setY(double y) { 
        return this.y = y;
    }

    // Flip the signs of the vector
    public HydraVector2d reflect() { 
        return new HydraVector2d(-x, -y);
    }

    // Returns vector reflected into the 1st quadrant
    public HydraVector2d abs() { 
        return new HydraVector2d(Math.abs(x), Math.abs(y));
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() { 
        double magnitude= getLength();
        x /= magnitude;
        y /= magnitude;
    }

    public HydraVector2d getUnitVector() {
        return normalize(1);
    }

    //returns a HydraVector2d in the same direction with magnitude of "target"
    public HydraVector2d normalize(double target) {
        if (getMagnitude() == 0) return ZERO; //avoid dividing by zero
        return scale(target / getMagnitude());
    }

    public HydraVector2d getNormalized() {
        double magnitude = getLength();
        return new HydraVector2d(x / magnitude, y / magnitude);
    }

    //normalizes a group of vectors so that they maintain the same relative magnitudes and ...
    // the vector of largest magnitude now has a magnitude equal to limit
    public static HydraVector2d[] batchNormalize(double limit, HydraVector2d... vecs) {
        double maxMag = 0;
        for (HydraVector2d v : vecs) {
            if (v.getMagnitude() > maxMag) {
                maxMag = v.getMagnitude();
            }
        }
        if (limit >= maxMag) {
            return vecs;
        }
        HydraVector2d[] normed = new HydraVector2d[vecs.length];
        for (int i = 0; i < vecs.length; i++) {
            normed[i] = vecs[i].scale(limit / maxMag);
        }
        return normed;
    }

    // Override toString method to provide a string representation of the vector
    @Override
    public String toString() {
        // Format the vector as a string with two decimal places for components
        return String.format("{%.2f, %.2f}", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HydraVector2d)) {
            return false;
        }
        HydraVector2d other = (HydraVector2d) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }
}