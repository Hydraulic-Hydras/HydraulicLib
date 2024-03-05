package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

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
    public HydraPIDFController controller;

    /**
     * Constructor for HydraMotionProfiledDcMotor.
     *
     * @param hardwareMap HardwareMap instance to get motor from
     * @param name        Name of the motor on the hardware map
     */
    public HydraMotionProfiledDcMotor(HardwareMap hardwareMap, String name) {
        motor = hardwareMap.get(DcMotorEx.class, name);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        RETRACTION_MULTIPLIER = 1.0;
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
     */
    public void setPIDCoefficients(double kP, double kI, double kD, double kF) {
        HydraPIDCoefficients coeffs = new HydraPIDCoefficients(kP, kI, kD);
        this.controller = new HydraPIDFController(coeffs, 0, 0, 0, (position, velocity) -> kF);
        // Automatically sets kV, kA, kStatic to 0.0 and position and velocity to KF
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
    }

    /**
     * Executes the PIDF controller to set the motor power based on the target position.
     * Update must be called for the motor to gain power.
     */
    public void update() {
        // Run the PIDF controller to get the motor power
        double power = controller.update(motor.getCurrentPosition(), motor.getVelocity());

        // Set the motor power
        motor.setPower(power * RETRACTION_MULTIPLIER);

        // Check if the motion profile has finished
        if (motionProfile.isFinished(profileTimer.seconds())) {
            // Stop the motor
            motor.setPower(0);
            // Reset the profile timer for future use
            profileTimer.reset();
        }
    }

    /**
     * Returns an indication of the manufacturer of this device.
     *
     * @return the device's manufacturer
     */
    @Override
    public Manufacturer getManufacturer() {
        return motor.getManufacturer();
    }

    /**
     * Returns a string suitable for display to the user as to the type of device.
     * Note that this is a device-type-specific name; it has nothing to do with the
     * name by which a user might have configured the device in a robot configuration.
     *
     * @return device manufacturer and name
     */
    @Override
    public String getDeviceName() {
        return motor.getDeviceName();
    }

    /**
     * Get connection information about this device in a human readable format
     *
     * @return connection info
     */
    @Override
    public String getConnectionInfo() {
        return motor.getConnectionInfo();
    }

    /**
     * Version
     *
     * @return get the version of this device
     */
    @Override
    public int getVersion() {
        return motor.getVersion();
    }

    /**
     * Resets the device's configuration to that which is expected at the beginning of an OpMode.
     * For example, motors will reset the their direction to 'forward'.
     */
    @Override
    public void resetDeviceConfigurationForOpMode() {
        motor.resetDeviceConfigurationForOpMode();
    }

    /**
     * Closes this device
     */
    @Override
    public void close() {
        motor.close();
    }

    /**
     * Sets the logical direction in which this motor operates.
     *
     * @param direction the direction to set for this motor
     * @see #getDirection()
     */
    @Override
    public void setDirection(Direction direction) {
        motor.setDirection(direction);
    }

    /**
     * Returns the current logical direction in which this motor is set as operating.
     *
     * @return the current logical direction in which this motor is set as operating.
     * @see #setDirection(Direction)
     */
    @Override
    public Direction getDirection() {
        return motor.getDirection();
    }

    /**
     * Sets the power level of the motor, expressed as a fraction of the maximum
     * possible power / speed supported according to the run mode in which the
     * motor is operating.
     *
     * <p>Setting a power level of zero will brake the motor</p>
     *
     * @param power the new power level of the motor, a value in the interval [-1.0, 1.0]
     * @see #getPower()
     * @see DcMotor#setMode(DcMotor.RunMode)
     * @see DcMotor#setPowerFloat()
     */
    @Override
    public void setPower(double power) {
        motor.setPower(power);
    }

    /**
     * Returns the current configured power level of the motor.
     *
     * @return the current level of the motor, a value in the interval [0.0, 1.0]
     * @see #setPower(double)
     */
    @Override
    public double getPower() {
        return motor.getPower();
    }

    /**
     * Returns the current consumed by this motor.
     * @param unit current units
     * @return the current consumed by this motor.
     */
    public double getCurrent(CurrentUnit unit) {
        return motor.getCurrent(unit);
    }
}