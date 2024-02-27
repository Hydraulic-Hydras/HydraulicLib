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
public class HydraRobot {

    /**
     * HardwareMap circumstance for a Robot class.
     */
    public HardwareMap hardwareMap;

    /**
     * Telemetry circumstance for a Robot class.
     */
    public Telemetry telemetry;

    public HydraRobot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

}
