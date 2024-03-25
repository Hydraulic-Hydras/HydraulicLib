package com.hydraulichydras.hydralib;

public class HydraPositionCommand extends HydraCommandFoundation {

    public HydraPose pose;
    public HydraDrivetrain drivetrain;

    public HydraPositionCommand(HydraPose pose, HydraMecanumDrivetrain drivetrain) {
        this.pose = pose;
        this.drivetrain = drivetrain;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean disrupt) {
        drivetrain.set(new HydraPose());
    }

}
