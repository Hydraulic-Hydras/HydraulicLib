package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Abstract class representing an OpMode designed to run Hydra commands.
 */
public abstract class HydraCommandOpMode extends LinearOpMode {

    // Flag indicating if the robot is disabled
    public static boolean isDisabled = false;

    // Resets the command machine instance
    public void reset() {
        HydraCommandMachine.getInstance().reset();
    }

    // Runs the command machine
    public void run() {
        HydraCommandMachine.getInstance().run();
    }

    // Schedules Hydra commands
    public void schedule(HydraCommand... commands) {
        HydraCommandMachine.getInstance().schedule(commands);
    }

    // Registers Hydra subsystems
    public void register(HydraSubsystem... subsystems) {
        HydraCommandMachine.getInstance().registerHydraSubsystem(subsystems);
    }

    // Overrides the runOpMode method of LinearOpMode
    @Override
    public void runOpMode() throws InterruptedException {
        initialize(); // Initializes the OpMode

        waitForStart(); // Waits for the start command from the driver station

        // Runs the command machine until stop is requested or the OpMode is inactive
        while (!isStopRequested() && opModeIsActive()) {
            run(); // Executes the command machine
        }

        reset(); // Resets the command machine
    }

    // Abstract method to be implemented by subclasses to initialize the OpMode
    public abstract void initialize();

    // Disables the robot
    public static void disable() {
        isDisabled = true;
    }

    // Enables the robot
    public static void enable() {
        isDisabled = false;
    }

}