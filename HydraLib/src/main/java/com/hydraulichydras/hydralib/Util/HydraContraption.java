package com.hydraulichydras.hydralib.Util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Contraption serves as an abstract blueprint encompassing shared methods and/or instance variables
 * applicable to every contraption on a robot.
 * <p>
 * Any robotic contraption, inclusive of the primary hardware map, is expected to inherit from this
 * abstract class and put into your "Robot" class.
 */
public abstract class HydraContraption {

    /**
     * OpMode circumstance for a Contraption class.
     */
    protected LinearOpMode opMode;

    /**
     * Telemetry circumstance for a Contraption class.
     */
    protected Telemetry telemetry;

    /**
     * Initiates the robot's hardware by obtaining and storing references to the robot configuration.
     * It also establishes the initial positions for motors and servos.
     *
     * @param hwMap robot's hardware map
     */
    public abstract void initialize(HardwareMap hwMap);

    /**
     * Handles gamepad inputs and orchestrates the corresponding responses from the contraptions.
     * <p>
     * Implement when employing a solitary gamepad in slot 1.
     */
    public void loop(Gamepad gamepad1) {}

    /**
     *
     * Handles various gamepad inputs and orchestrates the corresponding responses from the contraptions.
     * <p>
     * Implement when utilizing two gamepads.
     */
    public void loop(Gamepad gamepad1, Gamepad gamepad2) {}

    /**
     * Oversees the transmission of all telemetry data to the driver's phone or the FTC Dashboard.
     * <p>
     * Where all telemetry.addLine() or telemetry.addData() commands should go
     */
    public void telemetry(Telemetry telemetry) { }
}
