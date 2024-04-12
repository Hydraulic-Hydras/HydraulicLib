package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Map;

public interface HydraSensor {

    // Method signature for initializing the sensor
    void initialize(HardwareMap hardwareMap);

    // Method signature for reading data from the sensor
    void update();

    // Method signature for displaying sensor information
    void displayInfo();

    // Method signature to return state value
    boolean getState();

    // Method signature for setting States for digital devices
    void setState(boolean state);

    // Method signature for retrieving metadata about the sensor
    Map<String, Object> getMetadata();

}
