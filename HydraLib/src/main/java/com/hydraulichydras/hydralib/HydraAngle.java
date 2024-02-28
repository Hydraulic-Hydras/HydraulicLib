package com.hydraulichydras.hydralib;

// A utility class for handling angles and angle deltas
public class HydraAngle {

    // Constant representing Tau (2 * Pi)
    private static final double TAU = (Math.PI * 2);

    // Method to normalize an angle to the range [0, 2*Pi)
    public static double norm(double angle) {
        // Calculate the angle modulo Tau to ensure it's within [0, 2*Pi)
        double modifiedAngle = angle % TAU;
        // Ensure the result is positive by adding Tau and taking the modulo again
        modifiedAngle = (modifiedAngle + TAU) % TAU;
        return modifiedAngle;
    }

    // Method to normalize an angle delta to the range (-Pi, Pi]
    public static double normDelta(double angleDelta) {
        // Normalize the angle delta
        double modifiedAngleDelta = norm(angleDelta);
        // If the normalized delta is greater than Pi, subtract Tau to bring it into (-Pi, Pi]
        if (modifiedAngleDelta > Math.PI) {
            modifiedAngleDelta -= TAU;
        }
        return modifiedAngleDelta;
    }
}
