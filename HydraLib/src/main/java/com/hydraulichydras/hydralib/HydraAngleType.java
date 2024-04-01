package com.hydraulichydras.hydralib;

/**
 * Enumeration representing different types of angle representations.
 */
public enum HydraAngleType {
    /** Angles in the range [0, 360) in a Cartesian coordinate system. */
    ZERO_TO_360_CARTESIAN,

    /** Angles in the range [0, 360) in a heading coordinate system. */
    ZERO_TO_360_HEADING,

    /** Angles in the range [-180, 180] in a Cartesian coordinate system. */
    NEG_180_TO_180_CARTESIAN,

    /** Angles in the range [-180, 180] in a heading coordinate system. */
    NEG_180_TO_180_HEADING
}