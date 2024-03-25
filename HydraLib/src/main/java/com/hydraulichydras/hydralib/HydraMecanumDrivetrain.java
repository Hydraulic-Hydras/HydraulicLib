package com.hydraulichydras.hydralib;

import com.arcrobotics.ftclib.drivebase.RobotDrive;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Represents a Mecanum drivetrain with four motors configured in a H-drive configuration.
 * This class provides methods for controlling the drivetrain's movement and accessing telemetry data.
 * It allows for setting motor power, direction, and provides telemetry feedback of motor index values.
 */
public class HydraMecanumDrivetrain extends HydraSubsystem implements HydraDrivetrain {

    /**
     * DcMotor array for Drivetrain Class.
     */
    public DcMotorEx[] motors;

    public double[] poseSpeed = new double[4];

    public static HydraPIDFController translationalController = new HydraPIDFController(0, 0, 0);
    public static HydraPIDFController StrafingController = new HydraPIDFController(0, 0, 0);
    public static HydraPIDFController headingController = new HydraPIDFController(0, 0, 0);

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
        }
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

    @Override
    public void set(HydraPose pose) {
        set(pose, 0);
    }
    public void set(HydraPose pose, double angle) {
        set(pose.x, pose.y, pose.heading, angle);
    }
    public void set(double StrafeSpeed, double translationalSpeed, double rotationalSpeed, double gyroAngle) {

        HydraVector2d magnitude = new HydraVector2d(StrafeSpeed, translationalSpeed).rotation(-gyroAngle);

        StrafeSpeed = Range.clip(magnitude.x, -1, 1);
        translationalSpeed = Range.clip(magnitude.y, -1, 1);
        rotationalSpeed = Range.clip(rotationalSpeed, -1, 1);

        double[] motorSpeed = new double[4];

        motorSpeed[RobotDrive.MotorType.kFrontLeft.value] = translationalSpeed + StrafeSpeed + rotationalSpeed;
        motorSpeed[RobotDrive.MotorType.kFrontRight.value] = translationalSpeed - StrafeSpeed - rotationalSpeed;
        motorSpeed[RobotDrive.MotorType.kBackLeft.value] = (translationalSpeed - StrafeSpeed + rotationalSpeed);
        motorSpeed[RobotDrive.MotorType.kBackRight.value] = (translationalSpeed + StrafeSpeed - rotationalSpeed);

        double max = 1;
        for (double WheelSpeed : motorSpeed) max = Math.max(max, Math.abs(WheelSpeed));

        if (max > 1) {
            motorSpeed[RobotDrive.MotorType.kFrontLeft.value] /= max;
            motorSpeed[RobotDrive.MotorType.kFrontRight.value] /= max;
            motorSpeed[RobotDrive.MotorType.kBackLeft.value] /= max;
            motorSpeed[RobotDrive.MotorType.kBackRight.value] /= max;
        }

        poseSpeed[0] = motorSpeed[0];
        poseSpeed[1] = motorSpeed[1];
        poseSpeed[2] = motorSpeed[2];
        poseSpeed[3] = motorSpeed[3];
    }

    @Override
    public void write() {
        // This configuration is only for autonomous
        motors[0].setPower(poseSpeed[0]);
        motors[1].setPower(poseSpeed[2]);
        motors[2].setPower(poseSpeed[3]);
        motors[3].setPower(poseSpeed[1]);
    }

    public void setXController(double kP, double kI, double kD) {
        StrafingController.setPID(kP, kI, kD);
    }

    public void setYController(double kP, double kI, double kD) {
        translationalController.setPID(kP, kI, kD);
    }

    public void setHeadingController(double kP, double kI, double kD) {
        headingController.setPID(kP, kI, kD);
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

    @Override
    public void periodic() {
        // leave blank
    }

    @Override
    public void read() {
        // leave blank
    }

    @Override
    public void reset() {
        // leave blank
    }
}
