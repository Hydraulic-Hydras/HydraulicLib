package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;

/**
 * A DcMotorSimple implementation that supports motion profiling.
 */
public class HydraMotionProfiledDcMotor implements DcMotorSimple {

    /**
     * DcMotorEx instance for motor control
     */
    protected DcMotorEx motor;

    // Motion profile to manage the motor's motion
    private HydraMotionProfile motionProfile;
    public ElapsedTime profileTimer = new ElapsedTime();

    /**
     * Multiplier used for retracting motions, such as linear slides.
     * This multiplier scales down the maximum velocity and acceleration when retracting.
     */
    private double RETRACTION_MULTIPLIER;

    /**
     * PID controller for motion profile
     */
    public PIDFController controller;

    /**
     * Constructor for HydraMotionProfiledDcMotor.
     *
     * @param motor DcMotorEx instance to control motion
     */
    public HydraMotionProfiledDcMotor(DcMotorEx motor) {
        this.motor = motor;
    }

    /**
     * Initializes the motor from the hardware map and sets necessary motor modes for motion profiling.
     *
     * @param hardwareMap HardwareMap instance to get motor from
     * @param name        Name of the motor on the hardware map
     */
    public void initialize(HardwareMap hardwareMap, String name) {
        motor = hardwareMap.get(DcMotorEx.class, name);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RETRACTION_MULTIPLIER = 1.0;
    }

    /**
     * Sets the retraction multiplier.
     *
     * @param multiplier Retraction multiplier value
     */
    public void setRetractionMultiplier(double multiplier) {
        this.RETRACTION_MULTIPLIER = multiplier;
    }

    /**
     * Sets the maximum velocity and acceleration constraints for the motion profile.
     *
     * @param maxVel   Maximum velocity in inches per second
     * @param maxAccel Maximum acceleration in inches per second squared
     */
    public void setMotionProfileConstraints(double maxVel, double maxAccel) {
        motionProfile = new HydraMotionProfile(maxVel, maxAccel);
    }

    /**
     * Sets PID coefficients for the PIDF controller.
     *
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derivative gain
     * @param kF Feedforward gain
     */
    public void setPIDCoefficients(double kP, double kI, double kD, double kF) {
        PIDCoefficients coeffs = new PIDCoefficients(kP, kI, kD);
        this.controller = new PIDFController(coeffs, 0, 0, 0, (position, velocity) -> kF);
    }

    /**
     * Sets the target position for the motor and executes the motion profile.
     *
     * @param targetPosition Target position in encoder ticks
     */
    public void setTargetPosition(double targetPosition) {
        double startPosition = motor.getCurrentPosition();

        // Calculate motion profile
        motionProfile.calculateProfile(startPosition, targetPosition);

        // Reset profile timer
        profileTimer.reset();

        // Execute motion profile until completion
        while (!motionProfile.isFinished(profileTimer.seconds())) {
            double elapsedTime = profileTimer.seconds();
            double targetPower = motionProfile.getOutput(elapsedTime) * RETRACTION_MULTIPLIER;

            motor.setPower(targetPower);
        }

        // Stop motor after profile execution
        motor.setPower(0);
        // Reset profile timer for future use
        profileTimer.reset();
    }

    /**
     * Returns the current position of the motor.
     *
     * @return Current position in encoder ticks
     */
    public double getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    @Override
    public void setDirection(Direction direction) {
        motor.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return motor.getDirection();
    }

    @Override
    public void setPower(double power) {
        motor.setPower(power);
    }

    @Override
    public double getPower() {
        return motor.getPower();
    }

    @Override
    public Manufacturer getManufacturer() {
        return motor.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return motor.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return motor.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return motor.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        motor.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        motor.close();
    }
}
