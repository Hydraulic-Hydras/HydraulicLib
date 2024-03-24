package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class HydraCommandOpMode extends LinearOpMode {

    public static boolean isDisabled = false;

    public void reset() {
        HydraCommandMachine.getInstance().reset();
    }
    public void run() {
        HydraCommandMachine.getInstance().run();
    }
    public void schedule(HydraCommand... commands) {
        HydraCommandMachine.getInstance().schedule(commands);
    }
    public void register(HydraSubsystem... subsystems) {
        HydraCommandMachine.getInstance().registerHydraSubsystem(subsystems);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        waitForStart();

        // Run the Machine
        while (!isStopRequested() && opModeIsActive()) {
            run();
        }

        reset();
    }

    public abstract void initialize();

    public static void disable() {
        isDisabled = true;
    }

    public static void enable() {
        isDisabled = false;
    }

}
