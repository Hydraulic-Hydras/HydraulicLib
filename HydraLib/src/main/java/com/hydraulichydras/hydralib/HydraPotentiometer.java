package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a potentiometer sensor.
 */
public class HydraPotentiometer implements HydraSensor {

    private final String name;
    public AnalogInput analogInput;
    public double position;
    public double currentVoltage;

    /**
     * Constructor for the Potentiometer class.
     * @param name The name of the potentiometer.
     */
    public HydraPotentiometer(String name) {
        this.name = name;
    }

    /**
     * Initialize the potentiometer with the hardware map.
     * @param hardwareMap The hardware map to use for initialization.
     */
    @Override
    public void initialize(HardwareMap hardwareMap) {
        analogInput = hardwareMap.get(AnalogInput.class, name);
    }

    /**
     * Read the current position of the potentiometer.
     * This method should be called periodically
     */
    @Override
    public void update() {
        currentVoltage = analogInput.getVoltage();
        // Read the voltage from the potentiometer
        double voltage = analogInput.getVoltage();
        // Convert voltage to position in degrees
        position = ((voltage / 5.0) * 360.0) % 360;
    }

    /**
     * Display information about the potentiometer.
     */
    @Override
    public void displayInfo() {
        System.out.println("Potentiometer: " + name);
        System.out.println("Position: " + getPosition());
        System.out.println("Voltage: " + getCurrentVoltage());
    }

    /**
     * Get metadata about the potentiometer.
     * @return Metadata about the potentiometer.
     */
    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Type", "Potentiometer");
        metadata.put("Name", name);
        return metadata;
    }

    /**
     * Set the position of the potentiometer manually.
     * @param pos The position to set (in degrees).
     */
    public void setPosition(double pos) {
        position = pos;
    }

    /**
     * Get the current position of the potentiometer.
     * @return The current position of the potentiometer (in degrees).
     */
    public double getPosition() {
        return position;
    }

    /**
     * Get the current voltage of the potentiometer.
     */
    public double getCurrentVoltage() {
       return currentVoltage = analogInput.getVoltage();
    }

    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void setState(boolean state) {
        // no state
    }
}
