package com.hydraulichydras.hydralib;

public class HydraAngle {
    private static final double TAU = (Math.PI * 2);

    public static double norm(double angle) {
        double modifiedAngle = angle % TAU;
        modifiedAngle = (modifiedAngle + TAU) % TAU;
        return modifiedAngle;
    }

    public static double normDelta(double angleDelta) {
        double modifiedAngleDelta = norm(angleDelta);
        if (modifiedAngleDelta > Math.PI) {
            modifiedAngleDelta -= TAU;
        }
        return modifiedAngleDelta;
    }
}
