package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * The Robot class can be used to assign all your hardware objects such as motors, servos, sensors
 * etc. It is important to extend this class and have all your hardware initialized in one file
 * rather than separately initializing them in every OpMode.
 * </p>
 * You may find it helpful to extend this class to create your own "hardware robot" class.
 * You can extend separate mechanisms with the "HydraContraption" class and put them inside here too.
 */
public abstract class HydraRobot {

    /**
     * Initiates the robot's hardware by obtaining and storing references to the robot configuration.
     * </p>
     * @param hardwareMap robot's hardware map
     * @param telemetry robot's telemetry display.
     */
    public abstract void initialize(HardwareMap hardwareMap, Telemetry telemetry);

}