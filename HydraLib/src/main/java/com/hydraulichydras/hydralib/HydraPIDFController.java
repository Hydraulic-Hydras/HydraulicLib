package com.hydraulichydras.hydralib;

import com.acmerobotics.roadrunner.util.NanoClock;

import java.util.function.BiFunction;

/**
 * PID controller with various feedforward components.
 */
public class HydraPIDFController {

    public HydraPIDCoefficients coefficients;
    public double kV;
    public double kA;
    public double kStatic;
    public BiFunction<Double, Double, Double> kF;
    public NanoClock clock;

    public double errorSum = 0.0;
    public double lastUpdateTimestamp = Double.NaN;
    public boolean inputBounded = false;
    public double minInput = 0.0;
    public double maxInput = 0.0;
    public boolean outputBounded = false;
    public double minOutput = 0.0;
    public double maxOutput = 0.0;

    public double targetPosition = 0.0;
    public double targetVelocity = 0.0;
    public double targetAcceleration = 0.0;
    public double lastError = 0.0;

    public HydraPIDFController(HydraPIDCoefficients pid, double kV, double kA, double kStatic, BiFunction<Double, Double, Double> kF) {
        this.coefficients = pid;
        this.kV = kV;
        this.kA = kA;
        this.kStatic = kStatic;
        this.kF = kF;
        this.clock = clock != null ? clock : NanoClock.system();
    }

    public HydraPIDFController(double P, double I, double D) {
        coefficients.kP = P;
        coefficients.kI = I;
        coefficients.kD = D;
    }

    public void setPID(double P, double I, double D) {
        coefficients.kP = P;
        coefficients.kI = I;
        coefficients.kD = D;
    }
    public HydraPIDFController(HydraPIDCoefficients pid) {
        this.coefficients = pid;
        this.kV = 0.0;
        this.kA = 0.0;
        this.kStatic = 0.0;
        this.kF = (position, velocity) -> 0.0;
    }

    public double getLastError() {
        return lastError;
    }

    public void setInputBounds(double min, double max) {
        if (min < max) {
            inputBounded = true;
            minInput = min;
            maxInput = max;
        }
    }

    public void setOutputBounds(double min, double max) {
        if (min < max) {
            outputBounded = true;
            minOutput = min;
            maxOutput = max;
        }
    }

    private double getPositionError(double measuredPosition) {
        double error = targetPosition - measuredPosition;
        if (inputBounded) {
            double inputRange = maxInput - minInput;
            while (Math.abs(error) > inputRange / 2.0) {
                error -= Math.signum(error) * inputRange;
            }
        }
        return error;
    }

    public double update(double measuredPosition, Double measuredVelocity) {
        double currentTimestamp = clock.seconds();
        double error = getPositionError(measuredPosition);
        if (Double.isNaN(lastUpdateTimestamp)) {
            lastError = error;
            lastUpdateTimestamp = currentTimestamp;
            return 0.0;
        } else {
            double dt = currentTimestamp - lastUpdateTimestamp;
            errorSum += 0.5 * (error + lastError) * dt;
            double errorDeriv = (error - lastError) / dt;
            lastError = error;
            lastUpdateTimestamp = currentTimestamp;
            double baseOutput = coefficients.kP * error + coefficients.kI * errorSum +
                    coefficients.kD * (measuredVelocity != null ? targetVelocity - measuredVelocity : errorDeriv) +
                    kV * targetVelocity + kA * targetAcceleration + kF.apply(measuredPosition, measuredVelocity);
            double output = (Math.abs(baseOutput) < 1E-6) ? 0.0 : baseOutput + Math.signum(baseOutput) * kStatic;
            return outputBounded ? Math.max(minOutput, Math.min(output, maxOutput)) : output;
        }
    }

    public double update(double measuredPosition) {
        return update(measuredPosition, null);
    }

    public void reset() {
        errorSum = 0.0;
        lastError = 0.0;
        lastUpdateTimestamp = Double.NaN;
    }
}
