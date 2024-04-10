package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the HydraSensor interface for a beam brake sensor.
 */
public class HydraBeamBrake implements HydraSensor {

    private final String name;
    public DigitalChannel sensor;
    public boolean beamBroken;

    /**
     * Constructor for HydraBeamBrake.
     *
     * @param name The name of the sensor.
     */
    public HydraBeamBrake(String name) {
        this.name = name;
    }

    /**
     * Initializes the sensor.
     *
     * @param hardwareMap The hardware map to use for sensor initialization.
     */
    @Override
    public void initialize(HardwareMap hardwareMap) {
        sensor = hardwareMap.get(DigitalChannel.class, name);

        sensor.setMode(DigitalChannel.Mode.INPUT);
    }

    /**
     * Updates the state of the sensor.
     * This method should be called periodically to update the sensor's state.
     * It checks the state of the sensor and updates the beamBroken status accordingly.
     */
    @Override
    public void update() {
        beamBroken = getState();
    }

    /**
     * Displays information about the sensor.
     */
    @Override
    public void displayInfo() {
        System.out.println("Sensor: " + name);
        System.out.println("Beam Broken: " + sensor.getState());
    }

    /**
     * Gets the state of the sensor (beam broken or not).
     *
     * @return The state of the sensor.
     */
    @Override
    public boolean getState() {
        return sensor.getState();
    }

    /**
     * Sets the state of the sensor.
     *
     * @param state The state to set for the sensor.
     */
    @Override
    public void setState(boolean state) {
        sensor.setState(state);
    }

    /**
     * Gets metadata about the sensor.
     *
     * @return Metadata about the sensor.
     */
    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Type", "Beam Brake Sensor");
        metadata.put("Name", name);
        return metadata;
    }

    /**
     * Set the operating mode of the sensor.
     *
     * @param mode The mode to set for the sensor.
     */
    public void setMode(DigitalChannel.Mode mode) {
        sensor.setMode(mode);
    }
}
