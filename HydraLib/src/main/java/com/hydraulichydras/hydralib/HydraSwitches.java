package com.hydraulichydras.hydralib;

// Class to define Hydra controller buttons and triggers
public class HydraSwitches {

    // Enum to represent different buttons on the Hydra controller
    public enum HydraButton {
        CIRCLE,           // Circle button
        SQUARE,           // Square button
        TRIANGLE,         // Triangle button
        CROSS,            // Cross (X) button
        LEFT_BUMPER,      // Left bumper button
        RIGHT_BUMPER,     // Right bumper button
        SHARE,            // Share button
        OPTIONS,          // Options button
        DPAD_UP,          // Directional pad (D-pad) up button
        DPAD_DOWN,        // D-pad down button
        DPAD_LEFT,        // D-pad left button
        DPAD_RIGHT,       // D-pad right button
        LEFT_STICK_BUTTON,    // Left stick button
        RIGHT_STICK_BUTTON    // Right stick button
    }

    // Enum to represent left and right triggers on the Hydra controller
    public enum HydraTrigger {
        LEFT_TRIGGER,    // Left trigger
        RIGHT_TRIGGER    // Right trigger
    }
}
