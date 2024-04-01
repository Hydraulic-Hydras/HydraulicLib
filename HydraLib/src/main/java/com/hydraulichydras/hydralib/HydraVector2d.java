package com.hydraulichydras.hydralib;

/**
 *  A vector in 2D space typically consists of two components: one along the x-axis and one along the y-axis.
 *  This class is often used for geometric operations necessary.
 */
public class HydraVector2d {

    // Components of the vector
    public double x;
    public double y;

    // Predefined vector constants
    public final static HydraVector2d FORWARD = new HydraVector2d(0, 1);
    public final static HydraVector2d BACKWARD = new HydraVector2d(0, -1);
    public final static HydraVector2d LEFT = new HydraVector2d(-1, 0);
    public final static HydraVector2d RIGHT = new HydraVector2d(1, 1);
    public final static HydraVector2d ZERO = new HydraVector2d(0, 0);


    /**
     * Initializes a new vector with components set to (0, 0) representing the origin.
     */
    public HydraVector2d() {
        this(0.0, 0.0);
    }

    /**
     * Initializes a new vector with specified x and y components.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     */
    public HydraVector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Initializes a new unit vector with a specified angle.
     *
     * @param angle The angle of the vector in degrees.
     */
    public HydraVector2d(HydraAngle angle) {
        this.x = Math.cos(Math.toRadians(angle.convertAngle(HydraAngleType.NEG_180_TO_180_CARTESIAN).getAngle()));
        this.y = Math.sin(Math.toRadians(angle.convertAngle(HydraAngleType.NEG_180_TO_180_CARTESIAN).getAngle()));
        this.fixFloatingPointErrors();
    }

    /**
     * Creates a vector with polar coordinates.
     *
     * @param r     The magnitude of the vector.
     * @param theta The angle of the vector in radians.
     * @return A new HydraVector2d instance with the specified polar coordinates.
     */
    public static HydraVector2d polar(double r, double theta) {
        return new HydraVector2d(r * Math.cos(theta), r * Math.sin(theta));
    }

    /**
     * Computes the magnitude (length) of the vector.
     *
     * @return The magnitude of the vector.
     */
    public double norm() {
        return Math.hypot(x, y);
    }

    /**
     * Computes the angle (in radians) of the vector with respect to the positive x-axis.
     *
     * @return The angle of the vector in radians.
     */
    public double Angle() {
        return HydraAngle.norm(Math.atan2(y, x));
    }

    /**
     * Computes the angle (in radians) between this vector and another vector.
     *
     * @param other The other vector.
     * @return The angle between this vector and the other vector in radians.
     */
    public double angleBetween(HydraVector2d other) {
        return Math.acos((this.dot(other)) / (this.norm() * other.norm()));
    }

    /**
     * Computes the dot product of this vector with another vector.
     *
     * @param other The other vector.
     * @return The dot product of the two vectors.
     */
    public double dot(HydraVector2d other) {
        return getX() * other.getX() + getY() * other.getY();
    }

    /**
     * Multiplies the vector by a scalar.
     *
     * @param scalar The scalar value.
     * @return A new HydraVector2d instance representing the result of the multiplication.
     */
    public HydraVector2d scale(double scalar) {
        return new HydraVector2d(getX() * scalar, getY() * scalar);
    }

    /**
     * Divides the vector by a scalar.
     *
     * @param scalar The scalar value.
     * @return A new HydraVector2d instance representing the result of the division.
     * @throws IllegalArgumentException if the scalar value is zero.
     */
    public HydraVector2d divide(double scalar) {
        if (scalar == 0) throw new IllegalArgumentException("Division by zero");
        return new HydraVector2d(x / scalar, y / scalar);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other The other vector.
     * @return A new HydraVector2d instance representing the result of the subtraction.
     */
    public HydraVector2d subt(HydraVector2d other) {
        return new HydraVector2d(x - other.getX(), y - other.getY());
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The other vector.
     * @return A new HydraVector2d instance representing the result of the addition.
     */
    public HydraVector2d plus(HydraVector2d other) {
        return new HydraVector2d(x + other.getX(), y + other.getY());
    }

    /**
     * Computes the magnitude (length) of the vector.
     *
     * @return The magnitude of the vector.
     */
    public double magnitude() {
        return Math.hypot(x, y);
    }

    /**
     * Computes the magnitude (length) of the vector using the Pythagorean theorem.
     *
     * @return The magnitude of the vector.
     */
    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Computes the unit vector (vector with magnitude 1) in the same direction as this vector.
     *
     * @return The unit vector.
     */
    public HydraVector2d unit() {
        return this.divide(magnitude());
    }

    /**
     * Computes the angle (in radians) of the vector with respect to the positive x-axis.
     *
     * @return The angle of the vector in radians.
     */
    public double angle() {
        return HydraAngle.norm(Math.atan2(y, x));
    }

    /**
     * Returns the angle of the vector as an Angle object.
     *
     * @return The angle of the vector as an Angle object.
     */
    public HydraAngle getAngle() {
        double angRad = Math.atan2(y, x);
        return new HydraAngle(Math.toDegrees(angRad), HydraAngleType.NEG_180_TO_180_CARTESIAN);
    }

    /**
     * Returns the numerical value of the angle in the specified angle type.
     *
     * @param type The angle type to convert to.
     * @return The angle in the specified type.
     */
    public double getAngleDouble(HydraAngleType type) {
        return this.getAngle().convertAngle(type).getAngle();
    }

    /**
     * Rotates the vector by the specified angle counter-clockwise.
     *
     * @param angle The angle to rotate the vector by (in radians).
     * @return A new HydraVector2d instance representing the rotated vector.
     */
    public HydraVector2d rotation(double angle) {
        return new HydraVector2d(
                x * Math.cos(angle) - y * Math.sin(angle),
                x * Math.sin(angle) + y * Math.cos(angle)
        );
    }

    /**
     * Rotates the vector to the specified angle.
     *
     * @param ang The angle to rotate the vector to.
     * @return A new HydraVector2d instance representing the rotated vector.
     */
    public HydraVector2d rotateTo (HydraAngle ang) {
        return new HydraVector2d(ang).scale(this.getMagnitude());
    }

    /**
     * Rotates the vector by the specified angle (in degrees) in the specified direction.
     *
     * @param ang       The angle to rotate the vector by (in degrees).
     * @param direction The direction of rotation (CLOCKWISE or COUNTER_CLOCKWISE).
     * @return A new HydraVector2d instance representing the rotated vector.
     */
    public HydraVector2d rotateBy(double ang, HydraAngleDirection direction) {
        double angRads;
        if (direction == HydraAngleDirection.COUNTER_CLOCKWISE) {
            angRads = Math.toRadians(ang); //default vector rotation direction is CCW
        } else {
            angRads = -1 * Math.toRadians(ang);
        }
        return new HydraVector2d(x * Math.cos(angRads) - y * Math.sin(angRads), x * Math.sin(angRads) + y * Math.cos(angRads));
    }

    /**
     * Projects this vector onto another vector.
     *
     * @param other The vector onto which to project this vector.
     * @return A new HydraVector2d instance representing the projected vector.
     */
    public HydraVector2d project(HydraVector2d other) {
        double magnitude = other.magnitude();
        double angle = angle();
        return new HydraVector2d(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    /**
     * Projects this vector onto another vector.
     *
     * @param v The vector onto which to project this vector.
     * @return A new HydraVector2d instance representing the projected vector.
     */
    public HydraVector2d projection (HydraVector2d v) {
        return v.scale(dot(v)/(Math.pow(v.getMagnitude(), 2))); // u dot v over mag(v)^2 times v
    }

    /**
     * Fixes floating point errors in the vector components.
     */
    public void fixFloatingPointErrors() {
        if (Math.abs(this.x) < 1e-5) {
            this.x = 0;
        }
        if (Math.abs(this.y) < 1e-5) {
            this.y = 0;
        }
    }

    /**
     * Gets the x-coordinate of the vector.
     *
     * @return The x-coordinate of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the vector.
     *
     * @return The y-coordinate of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of the vector.
     *
     * @param x The new x-coordinate of the vector.
     * @return The new x-coordinate of the vector.
     */
    public double setX(double x) { 
        return this.x = x;
    }

    /**
     * Sets the y-coordinate of the vector.
     *
     * @param y The new y-coordinate of the vector.
     * @return The new y-coordinate of the vector.
     */
    public double setY(double y) { 
        return this.y = y;
    }

    /**
     * Returns a new HydraVector2d instance with the signs of its components flipped.
     *
     * @return The vector with flipped signs.
     */
    public HydraVector2d reflect() { 
        return new HydraVector2d(-x, -y);
    }

    /**
     * Returns a new HydraVector2d instance with the absolute values of its components.
     *
     * @return The vector with absolute values of components.
     */
    public HydraVector2d abs() { 
        return new HydraVector2d(Math.abs(x), Math.abs(y));
    }

    /**
     * Computes the length (magnitude) of the vector.
     *
     * @return The length (magnitude) of the vector.
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalizes the vector (i.e., scales it to have a magnitude of 1).
     */
    public void normalize() { 
        double magnitude= getLength();
        x /= magnitude;
        y /= magnitude;
    }

    /**
     * Computes the unit vector (vector with magnitude 1) in the same direction as this vector.
     *
     * @return The unit vector.
     */
    public HydraVector2d getUnitVector() {
        return normalize(1);
    }

    /**
     * Normalizes the vector to have the specified magnitude.
     *
     * @param target The target magnitude for the normalized vector.
     * @return The normalized vector with the specified magnitude.
     */
    public HydraVector2d normalize(double target) {
        if (getMagnitude() == 0) return ZERO; //avoid dividing by zero
        return scale(target / getMagnitude());
    }

    /**
     * Returns a new HydraVector2d instance that is normalized (i.e., scaled to have a magnitude of 1).
     *
     * @return The normalized vector.
     */
    public HydraVector2d getNormalized() {
        double magnitude = getLength();
        return new HydraVector2d(x / magnitude, y / magnitude);
    }

    /**
     * Normalizes a group of vectors so that they maintain the same relative magnitudes,
     * with the vector of largest magnitude having a specified limit.
     *
     * @param limit The magnitude limit for the largest vector after normalization.
     * @param vecs  The vectors to be normalized.
     * @return An array of HydraVector2d instances representing the normalized vectors.
     */
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