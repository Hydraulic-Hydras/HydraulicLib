package com.hydraulichydras.hydralib;

/**
 * Proportional, integral, and derivative (PID) gains used by HydraPIDFController.
 */
public class HydraPIDCoefficients {

    public double kP;
    public double kI;
    public double kD;

    public HydraPIDCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public HydraPIDCoefficients() {
        this.kP = 0.0;
        this.kI = 0.0;
        this.kD = 0.0;
    }
}
