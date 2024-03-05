package com.hydraulichydras.hydralib;

import static com.acmerobotics.roadrunner.util.MathUtilKt.epsilonEquals;

import java.util.ArrayList;
import java.util.List;


/**
 * A collection of methods for various kinematics-related tasks.
 */
public class HydraKinematics {

    /**
     * Returns the robot pose velocity corresponding to fieldPose and fieldVel.
     */
    public static HydraPose fieldToRobotVelocity(HydraPose fieldPose, HydraPose fieldVel) {
        return new HydraPose(fieldVel.vec().rotation(-fieldPose.heading), fieldVel.heading);
    }

    /**
     * Returns the robot pose acceleration corresponding to fieldPose, fieldVel, and fieldAccel.
     */
    public static HydraPose fieldToRobotAcceleration(HydraPose fieldPose, HydraPose fieldVel, HydraPose fieldAccel) {
        HydraPose fieldAccelRotated = new HydraPose(fieldAccel.vec().rotation(-fieldPose.heading), fieldAccel.heading);

        double cosTheta = Math.cos(fieldPose.heading);
        double sinTheta = Math.sin(fieldPose.heading);

        double x = -fieldVel.getX() * sinTheta + fieldVel.getY() * cosTheta;
        double y = -fieldVel.getX() * cosTheta - fieldVel.getY() * sinTheta;
        double heading = fieldVel.getHeading();

        return fieldAccelRotated.add(new HydraPose(x, y, 0.0)).add(new HydraPose(0.0, 0.0, heading));
    }

    /**
     * Returns the error between targetFieldPose and currentFieldPose.
     */
    public static HydraPose calculatePoseError(HydraPose targetFieldPose, HydraPose currentFieldPose) {
        HydraVector2d vecError = targetFieldPose.subt(currentFieldPose).vec();
        double headingError = HydraAngle.normDelta(targetFieldPose.heading - currentFieldPose.heading);
        return new HydraPose(vecError.rotation(-currentFieldPose.heading), headingError);
    }

    /**
     * Computes the motor feedforward (i.e., open loop powers) for the given set of coefficients.
     */
    public static List<Double> calculateMotorFeedforward(List<Double> vels, List<Double> accels, double kV, double kA, double kStatic) {
        List<Double> feedforwards = new ArrayList<>();
        for (int i = 0; i < vels.size(); i++) {
            double vel = vels.get(i);
            double accel = accels.get(i);
            feedforwards.add(calculateMotorFeedforward(vel, accel, kV, kA, kStatic));
        }
        return feedforwards;
    }

    /**
     * Computes the motor feedforward (i.e., open loop power) for the given set of coefficients.
     */
    public static double calculateMotorFeedforward(double vel, double accel, double kV, double kA, double kStatic) {
        double basePower = vel * kV + accel * kA;
        if (epsilonEquals(basePower, 0.0)) {
            return 0.0;
        } else {
            return basePower + Math.signum(basePower) * kStatic;
        }
    }

    /**
     * Performs a relative odometry update. Note: this assumes that the robot moves with constant velocity over the measurement interval.
     */
    public static HydraPose relativeOdometryUpdate(HydraPose fieldPose, HydraPose robotPoseDelta) {
        double dtheta = robotPoseDelta.heading;
        double sineTerm, cosTerm;
        if (epsilonEquals(dtheta, 0.0)) {
            sineTerm = 1.0 - dtheta * dtheta / 6.0;
            cosTerm = dtheta / 2.0;
        } else {
            sineTerm = Math.sin(dtheta) / dtheta;
            cosTerm = (1 - Math.cos(dtheta)) / dtheta;
        }

        double x = sineTerm * robotPoseDelta.getX() - cosTerm * robotPoseDelta.getY();
        double y = cosTerm * robotPoseDelta.getX() + sineTerm * robotPoseDelta.getY();

        HydraVector2d fieldPositionDelta = new HydraVector2d(x, y).rotation(fieldPose.heading);
        HydraPose fieldPoseDelta = new HydraPose(fieldPositionDelta, robotPoseDelta.heading);

        return new HydraPose(
                fieldPose.getX() + fieldPoseDelta.getX(),
                fieldPose.getY() + fieldPoseDelta.getY(),
                HydraAngle.norm(fieldPose.heading + fieldPoseDelta.heading)
        );
    }
}