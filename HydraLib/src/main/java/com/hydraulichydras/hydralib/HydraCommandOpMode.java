package com.hydraulichydras.hydralib;

/*
 * An OpMode class built for running a Command Based OpMode.
 */
public abstract class HydraCommandOpMode extends LinearOpMode {


    // Ovverides the basic runOpMode method to implement commands.
    @Override
    public void runOpMode() throws InterruptedException  { 
        initialize();

        waitForStart();

        // Run the Commands
        while (!isStopRequested() && opModeIsActive()) { 
            run();
        }

        reset();
    }

    public abstract void initialize(); 

    public static void disable() { 
        Robot.disable();
    }

    public static void enable() { 
        Robot.enable();
    }
    
}
