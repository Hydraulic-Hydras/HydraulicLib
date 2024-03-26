package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Represents a Mecanum drivetrain with four motors configured in a H-drive configuration.
 * This class provides methods for controlling the drivetrain's movement and accessing telemetry data.
 * It allows for setting motor power, direction, and provides telemetry feedback of motor index values.
 */
public class HydraMecanumDrivetrain {

    /**
     * DcMotor array for Drivetrain Class.
     */
    public DcMotorEx[] motors;

    /**
     * Constants for drivetrain localization
     */
    public double WHEEL_RADIUS;
    public double TICKS_PER_REV;

    /**
     * Default Pose setup
     */
    public HydraPose pose;

    /**
     * Constructs a new HydraMecanumDrivetrain with the provided DcMotors.
     * @param leftFront The DcMotor for the left front wheel with Index 0.
     * @param leftRear The DcMotor for the left rear wheel with Index 1.
     * @param rightRear The DcMotor for the right rear wheel with Index 2.
     * @param rightFront The DcMotor for the right front wheel with Index 3.
     */
    public HydraMecanumDrivetrain(DcMotorEx leftFront, DcMotorEx leftRear, DcMotorEx rightRear, DcMotorEx rightFront) {
        this.motors = new DcMotorEx[] {leftFront, leftRear, rightRear, rightFront};

        assignMotor(leftFront, 0);
        assignMotor(leftRear, 1);
        assignMotor(rightRear, 2);
        assignMotor(rightFront, 3);

        // Set zero power behavior to BRAKE
        for (DcMotor motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        pose = new HydraPose();
    }

    /**
     * Sets the motor power based on the gamepad input for Mecanum drive.
     * @param gamepad The gamepad object to read input from.
     */
    public void setDriverControl(Gamepad gamepad) {

        double VerticalTranslation = -gamepad.left_stick_y;
        double HorizontalTranslation = gamepad.left_stick_x;
        double Pivoting = gamepad.right_stick_x;

        double leftFrontSpeed = (VerticalTranslation + HorizontalTranslation + Pivoting);
        double leftRearSpeed = ((VerticalTranslation - HorizontalTranslation) + Pivoting);

        double rightRearSpeed = ((VerticalTranslation + HorizontalTranslation) - Pivoting);
        double rightFrontSpeed = ((VerticalTranslation - HorizontalTranslation) - Pivoting);

        motors[0].setPower(leftFrontSpeed);
        motors[1].setPower(leftRearSpeed);
        motors[2].setPower(rightRearSpeed);
        motors[3].setPower(rightFrontSpeed);
    }

    /**
     * Sets the Constants for the Drivetrain
     *
     * @param WHEEL_RADIUS The Radius of your wheels
     * @param TICKS_PER_REV  The encoder ticks per revolution of a motor
     */
    public void setConstants(double WHEEL_RADIUS, double TICKS_PER_REV) {
        this.WHEEL_RADIUS = WHEEL_RADIUS;
        this.TICKS_PER_REV = TICKS_PER_REV;
    }

    /**
     * Sets the direction of a motor.
     * @param motorIndex The index of the motor in the motors array.
     * @param direction The direction to set for the motor.
     * <p></p>
     * motorIndex is based of the constructor, to clear up any confusion
     * the index 0 of any constructor will always be the LeftFront motor assigned.
     */
    public void setMotorDirection(int motorIndex, DcMotorSimple.Direction direction) {
        motors[motorIndex].setDirection(direction);
    }

    /**
     * Sets the power of a motor.
     * @param motorIndex The index of the motor in the motors array.
     * @param power The power to set for the motor.
     * <p></p>
     * motorIndex is based of the constructor, to clear up any confusion
     * the index 0 of any constructor will always be the LeftFront motor assigned.
     */
    public void setMotorPower(int motorIndex, double power) {
        motors[motorIndex].setPower(power);
    }

    /**
     * Assigns a motor to a specific index in the motors array.
     * @param motor The motor to assign.
     * @param index The index to assign the motor to.
     * <p></p>
     * motorIndex is based of the constructor, to clear up any confusion
     * the index 0 of any constructor will always be the LeftFront motor assigned.
     */
    public void assignMotor(DcMotorEx motor, int index) {
        if (index >= 0 && index < 4) {
            motors[index] = motor;
        }
    }

    /**
     * Updates telemetry with the index values of each motor.
     * @param telemetry The telemetry object to update.
     */
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("LeftFront index value: ", motors[0]);
        telemetry.addData("LeftRear index value: ", motors[1]);
        telemetry.addData("RightRear index value: ", motors[2]);
        telemetry.addData("RightFront index value: ", motors[3]);
        telemetry.update();
    }

    /**
     * Method to set a specified HydraPose
     *
     * @param x The x-axis
     * @param y The y-axis
     * @param heading The heading (in Radians)
     */
    public HydraPose setPoseEstimate(double x, double y, double heading) {
        return pose = new HydraPose(x, y, heading);
    }

    /**
     * Retrieves the encoder readings of the chassis and converts it to a HydraPose
     * Should work! (Im assuming)
     */
    public void getPoseEstimate() {
        // Get encoder ticks for each motor
        int ticksFL = motors[0].getCurrentPosition();
        int ticksFR = motors[3].getCurrentPosition();
        int ticksBL = motors[1].getCurrentPosition();
        int ticksBR = motors[2].getCurrentPosition();

        // Calculate distance traveled by each wheel
        double distanceFL = (ticksFL / TICKS_PER_REV) * (2 * Math.PI * WHEEL_RADIUS);
        double distanceFR = (ticksFR / TICKS_PER_REV) * (2 * Math.PI * WHEEL_RADIUS);
        double distanceBL = (ticksBL / TICKS_PER_REV) * (2 * Math.PI * WHEEL_RADIUS);
        double distanceBR = (ticksBR / TICKS_PER_REV) * (2 * Math.PI * WHEEL_RADIUS);

        // Calculate the x and y components of the robot's movement
        double deltaX = (distanceFL + distanceFR + distanceBL + distanceBR) / 4.0;
        double deltaY = (distanceFL - distanceFR + distanceBL - distanceBR) / 4.0;

        // Calculate the change in heading based on the difference between front and back wheel movements
        double deltaTheta = (distanceFL - distanceFR - distanceBL + distanceBR) / (4.0 * WHEEL_RADIUS);

        // Update pose
        double heading = pose.getHeading(); // Get current heading
        double newX = pose.getX() + deltaX * Math.cos(heading) - deltaY * Math.sin(heading);
        double newY = pose.getY() + deltaX * Math.sin(heading) + deltaY * Math.cos(heading);
        double newHeading = heading + deltaTheta;

        // Normalize heading angle to [-pi, pi]
        newHeading = Math.atan2(Math.sin(newHeading), Math.cos(newHeading));

        // Update pose
        HydraPose newPose = new HydraPose(newX, newY, newHeading);
        pose.set(newPose);
    }
}
