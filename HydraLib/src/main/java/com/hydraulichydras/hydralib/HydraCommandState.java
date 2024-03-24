package com.hydraulichydras.hydralib;

public class HydraCommandState {

    private final boolean disrupt;

    public HydraCommandState(boolean disrupt) {
        this.disrupt = disrupt;
    }

    public boolean isDisrupted() {
        return disrupt;
    }
}
