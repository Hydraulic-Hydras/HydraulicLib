package com.hydraulichydras.hydralib;

// A utility class for handling angles and angle deltas
public class HydraAngle {

    // Relative to robot starting position (right = east, left = west, forward = north, backward = south)
    public static final HydraAngle
            RIGHT = new HydraAngle(90, HydraAngleType.NEG_180_TO_180_HEADING),
            LEFT = new HydraAngle(-90, HydraAngleType.NEG_180_TO_180_HEADING),
            BACKWARD = new HydraAngle(180, HydraAngleType.NEG_180_TO_180_HEADING),
            FORWARD = new HydraAngle(0, HydraAngleType.NEG_180_TO_180_HEADING);

    /** The angle value. */
    private double angle;

    /** The type of angle representation. */
    private final HydraAngleType type;

    /** The type of angle representation. */
    private static final double TAU = (Math.PI * 2);

    /**
     * Constructs a HydraAngle object with the given angle and type.
     *
     * @param angle The angle value.
     * @param type  The type of angle representation.
     */
    public HydraAngle (double angle, HydraAngleType type) {
        this.angle = angle;
        this.type = type;

        //handles case of input angle outside of range (ex. angle = 600)
        this.angle = convertAngleDouble(type);
    }

    /**
     * Gets the angle value in the specified type.
     *
     * @param type The type of angle representation.
     * @return The angle value.
     */
    public double getAngle (HydraAngleType type) {
        return this.convertAngle(type).getAngle();
    }

    /**
     * Gets the angle value.
     *
     * @return The angle value.
     */
    public double getAngle () { return angle; }

    /**
     * Gets the type of angle representation.
     *
     * @return The type of angle representation.
     */
    public HydraAngleType getType () { return type; }

    /**
     * Converts the angle to the specified type. Assumes Degrees for input and output.
     *
     * @param outputType The type of angle representation to convert to.
     * @return The angle in the specified type.
     */
    public HydraAngle convertAngle (HydraAngleType outputType) {
        return new HydraAngle (convertAngleDouble(outputType), outputType);
    }

    /**
     * Normalizes an angle to the range [0, 2*Pi).
     *
     * @param angle The angle to be normalized.
     * @return The normalized angle within the range [0, 2*Pi).
     */
    public static double norm(double angle) {
        // Calculate the angle modulo Tau to ensure it's within [0, 2*Pi)
        double modifiedAngle = angle % TAU;
        // Ensure the result is positive by adding Tau and taking the modulo again
        modifiedAngle = (modifiedAngle + TAU) % TAU;
        return modifiedAngle;
    }

    /**
     * Normalizes an angle delta to the range (-Pi, Pi].
     *
     * @param angleDelta The angle delta to be normalized.
     * @return The normalized angle delta within the range (-Pi, Pi].
     */
    public static double normDelta(double angleDelta) {
        // Normalize the angle delta
        double modifiedAngleDelta = norm(angleDelta);
        // If the normalized delta is greater than Pi, subtract Tau to bring it into (-Pi, Pi]
        if (modifiedAngleDelta > Math.PI) {
            modifiedAngleDelta -= TAU;
        }
        return modifiedAngleDelta;
    }

    /**
     * Converts the angle to the specified type (double value).
     *
     * @param outputType The type of angle representation to convert to.
     * @return The angle value in the specified type.
     */
    public double convertAngleDouble (HydraAngleType outputType) {
        //handles case of same input and output type
        if (type == outputType) {
            return wrapAngle(this.getAngle(), outputType); // was new Angle(angle, type)
        }

        if (sameNumericalSystem(type, outputType)) {
            return convertCoordinateSystem(angle, type, outputType);
        }
        else if (sameCoordinateSystem(type, outputType)) {
            return convertNumericalSystem(angle, type, outputType);
        }
        else {
            //even though input and output types are not true to the type of intermediate angle...
            // they have the correct important characteristic (numerical or coordinate)
            double angleNewNumericalSystem = convertNumericalSystem(angle, type, numericalAndCoordinate(outputType, type)); //was type, output type
            return convertCoordinateSystem(angleNewNumericalSystem,
                    numericalAndCoordinate(outputType, type),
                    outputType);
        }
    }

    /**
     * Gets the absolute difference between this angle and another angle.
     * Min return value is 0 and Max return value is 180.
     *
     * @param other The other angle.
     * @return The absolute difference between the angles.
     */
    public double getDifference (HydraAngle other) {
        HydraAngle otherConverted = other.convertAngle(HydraAngleType.ZERO_TO_360_CARTESIAN);
        HydraAngle thisConverted = this.convertAngle(HydraAngleType.ZERO_TO_360_CARTESIAN);

        double rawDiff = Math.abs(otherConverted.getAngle() - thisConverted.getAngle());
        if (rawDiff > 180) {
            return 360 - rawDiff; //will be positive bc 360 is max rawDiff
        }
        return rawDiff; //number between  0 and 180
    }

    /**
     * Determines the direction of travel from this angle to another angle.
     *
     * @param other The other angle.
     * @return The direction of travel.
     */
    public HydraAngleDirection directionTo (HydraAngle other) {
        HydraAngle otherConverted = other.convertAngle(HydraAngleType.ZERO_TO_360_CARTESIAN);
        HydraAngle thisConverted = this.convertAngle(HydraAngleType.ZERO_TO_360_CARTESIAN);

        double rawDiff = Math.abs(otherConverted.getAngle() - thisConverted.getAngle());
        if (rawDiff > 180) {
            if (otherConverted.getAngle() > thisConverted.getAngle()) {
                return HydraAngleDirection.CLOCKWISE;
            } else {
                return HydraAngleDirection.COUNTER_CLOCKWISE;
            }
        } else {
            if (otherConverted.getAngle() > thisConverted.getAngle()) {
                return HydraAngleDirection.COUNTER_CLOCKWISE;
            } else {
                return HydraAngleDirection.CLOCKWISE;
            }
        }
    }


    /**
     * Rotates the angle by the specified amount and direction.
     * Passing a negative degrees will work, but will reverse the direction.
     * Direction should indicate positive direction of the angle system being used
     *
     * @param degrees   The amount of rotation in degrees.
     * @param direction The direction of rotation.
     * @return The rotated angle.
     */
    public HydraAngle rotateBy (double degrees, HydraAngleDirection direction) {
        HydraAngle thisConverted = this.convertAngle(HydraAngleType.ZERO_TO_360_HEADING);
        double newAngle;
        if (direction == HydraAngleDirection.CLOCKWISE) {
            newAngle = thisConverted.getAngle() + degrees;
        } else {
            newAngle = thisConverted.getAngle() - degrees;
        }
        return new HydraAngle(newAngle, HydraAngleType.ZERO_TO_360_HEADING).convertAngle(this.type);
    }

    /**
     * Rotates the angle by the specified amount in the positive direction.
     *
     * @param degrees The amount of rotation in degrees.
     * @return The rotated angle.
     */
    public HydraAngle rotateBy (double degrees) {
        return rotateBy(degrees, this.getPositiveDirection());
    }

    /**
     * Computes the average angle between two angles.
     *
     * @param angle1 The first angle.
     * @param angle2 The second angle.
     * @return The average angle.
     */
    public static HydraAngle getAverageAngle (HydraAngle angle1, HydraAngle angle2) {
        double difference = angle1.getDifference(angle2);
        HydraAngleDirection direction = angle1.directionTo(angle2);
        return angle1.rotateBy(difference/2.0, direction);
    }

    /** INTERNAL METHODS **/
    // input and output type should have the same numerical system
    public static double convertCoordinateSystem (double inputAngle, HydraAngleType inputType, HydraAngleType outputType) {
        //ensure input and output coordinate system not same- assumed different later on (bc of *-1)
        if (sameCoordinateSystem(inputType, outputType)) {
            return inputAngle; //not sure about this
        }

        if (isCartesian(inputType)) {
            // +90 is to convert coordinate systems
            // WrapAngle is to make sure within bounds of numerical system
            // *-1 or 360- is to flip direction (coordinate system change always causes positive to flip between CW and CCW)
            if (isZeroTo360(inputType)) {
                return 360 - wrapAngle(inputAngle - 90, outputType); //flipped plus to minus (correct with minus)
            }
            else {
                return -1 * wrapAngle(inputAngle - 90, outputType);
            }
        } else { //input type is heading system
            if (isZeroTo360(inputType)) {
                return 360 - wrapAngle(inputAngle - 90, outputType); //WAS +90
            }
            else {
                return -1 * wrapAngle(inputAngle - 90, outputType); //WAS +90
            }
        }
    }

    public static double convertNumericalSystem (double inputAngle, HydraAngleType inputType, HydraAngleType outputType) {
        if (sameNumericalSystem(inputType, outputType)) {
            return inputAngle; //for uniformity
        }
        return wrapAngle(inputAngle, outputType);
    }

    public static boolean sameCoordinateSystem(HydraAngleType firstType, HydraAngleType secondType) {
        return isCartesian(firstType) == isCartesian(secondType);
    }

    public static boolean sameNumericalSystem(HydraAngleType firstType, HydraAngleType secondType) {
        return isZeroTo360(firstType) == isZeroTo360(secondType);
    }

    public static boolean isCartesian (HydraAngleType angleType) {
        return angleType == HydraAngleType.ZERO_TO_360_CARTESIAN || angleType == HydraAngleType.NEG_180_TO_180_CARTESIAN;
    }

    public static boolean isZeroTo360 (HydraAngleType angleType) {
        return angleType == HydraAngleType.ZERO_TO_360_CARTESIAN || angleType == HydraAngleType.ZERO_TO_360_HEADING;
    }

    public static HydraAngleType numericalAndCoordinate (HydraAngleType numericalType, HydraAngleType coordinateType) {
        if (isZeroTo360(numericalType) && isCartesian(coordinateType)) return HydraAngleType.ZERO_TO_360_CARTESIAN;
        else if (!isZeroTo360(numericalType) && isCartesian(coordinateType)) return HydraAngleType.NEG_180_TO_180_CARTESIAN;
        else if (isZeroTo360(numericalType) && !isCartesian(coordinateType)) return HydraAngleType.ZERO_TO_360_HEADING;
        else return HydraAngleType.NEG_180_TO_180_HEADING; //!isZeroTo360(numericalType) && !isCartesian(coordinateType)
    }

    public HydraAngleDirection getPositiveDirection () {
        if (this.type == HydraAngleType.NEG_180_TO_180_HEADING || this.type == HydraAngleType.ZERO_TO_360_HEADING) {
            return HydraAngleDirection.CLOCKWISE;
        }
        return HydraAngleDirection.COUNTER_CLOCKWISE;
    }

    // returns an angle between max and min, assuming a coordinate system starting at min and wrapping back to max
    // Assumes min < max AND min <= 0
    public static double wrapAngle(double angle, double min, double max) {
        angle = mod(angle, range(min, max));
        if (angle > max) { //won't be < min bc of second assumption
            return min + min + angle; //I have no idea why, but it seems to work for all cases under assumptions (?)
        }
        return angle;
    }

    // Shortcut for AngleType instead of min and max bounds
    public static double wrapAngle(double angle, HydraAngleType outputAngleType) {
        if (isZeroTo360(outputAngleType)) {
            return wrapAngle(angle, 0, 360);
        } else {
            return wrapAngle(angle, -180, 180);
        }
    }

    // Returns the range between two numbers (ex. -180, 180 returns 360)
    public static double range (double num1, double num2) {
        return Math.abs(num1-num2);
    }

    // Returns python version of n % m (n is dividend, m is divisor)
    // Python % never returns negative numbers, but Java % does
    public static double mod (double n, double m) {
        return (((n % m) + m) % m);
    }
}
