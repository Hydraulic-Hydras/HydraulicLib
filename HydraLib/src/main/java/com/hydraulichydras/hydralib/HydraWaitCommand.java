package com.hydraulichydras.hydralib;

import com.arcrobotics.ftclib.util.Timing.Timer;

import java.util.concurrent.TimeUnit;

/**
 * Represents a command that waits for a specified amount of time before finishing.
 */
public class HydraWaitCommand extends HydraCommandFoundation {

    // Timer to track the elapsed time
    protected Timer timer;

    // Constructor to initialize the wait command with a specified duration in milliseconds
    public HydraWaitCommand(long millis) {
        timer = new Timer(millis, TimeUnit.MILLISECONDS); // Initialize the timer with the specified duration
        setName(name + ":" + millis + " milliseconds"); // Set the command name including the duration
    }

    // Initializes the command by starting the timer
    @Override
    public void initialize() {
        timer.start();
    }

    // Ends the command by pausing the timer
    @Override
    public void end(boolean interrupted) {
        timer.pause();
    }

    // Checks if the command is finished (timer has elapsed)
    @Override
    public boolean isFinished() {
        return timer.done();
    }

    // Indicates that the command can run even when the robot is disabled
    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}