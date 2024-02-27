package com.hydraulichydras.hydralib.Drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class HydraMecanumDrivetrain {

    public DcMotor[] motors;

    /**
     * Constructs a new HydraMecanumDrivetrain with the provided DcMotors.
     * @param leftFront The DcMotor for the left front wheel with Index 0.
     * @param leftRear The DcMotor for the left rear wheel with Index 1.
     * @param rightRear The DcMotor for the right rear wheel with Index 2.
     * @param rightFront The DcMotor for the right front wheel with Index 3.
     */
    public HydraMecanumDrivetrain(DcMotor leftFront, DcMotor leftRear, DcMotor rightRear, DcMotor rightFront) {
        this.motors = new DcMotor[] {leftFront, leftRear, rightRear, rightFront};

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
    public void assignMotor(DcMotor motor, int index) {
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
}
