package com.hydraulichydras.hydralib;

public class HydraDirectCommand extends HydraCommandFoundation {
    private final Runnable toRun;

    public HydraDirectCommand(Runnable toRun, HydraSubsystem... requirements) {
        this.toRun = toRun;

        addRequirements(requirements);
    }

    public HydraDirectCommand() {
        toRun = () -> {
        };
    }

    @Override
    public void initialize() {
       toRun.run();
    }

    @Override
    public final boolean isFinished() {
        return true;
    }
}
