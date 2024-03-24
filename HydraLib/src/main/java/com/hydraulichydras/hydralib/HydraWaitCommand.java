package com.hydraulichydras.hydralib;

import com.arcrobotics.ftclib.util.Timing.Timer;

import java.util.concurrent.TimeUnit;

public class HydraWaitCommand extends HydraCommandFoundation {

    protected Timer timer;

    public HydraWaitCommand(long millis) {
        timer = new Timer(millis, TimeUnit.MILLISECONDS);
        setName(name + ":" + millis + " milliseconds");
    }

    @Override
    public void initialize() {
        timer.start();
    }

    @Override
    public void end(boolean interrupted) {
        timer.pause();
    }

    @Override
    public boolean isFinished() {
        return timer.done();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}
