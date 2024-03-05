package com.hydraulichydras.hydralib;

import java.util.ArrayList;
import java.util.List;

/**
 * Swerve drive kinematic equations. All wheel positions and velocities are given starting with front left and
 * proceeding counter-clockwise (i.e., front left, rear left, rear right, front right). Robot poses are specified in a
 * coordinate system with positive x pointing forward, positive y pointing left, and positive heading measured
 * counter-clockwise from the x-axis.
 */
public class HydraSwerveKinematics {
    /**
     * Computes the wheel velocity vectors corresponding to robotVel given the provided trackWidth and
     * wheelBase.
     *
     * @param robotVel   velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase  distance between pairs of wheels on the same side of the robot
     */
    public static List<HydraVector2d> robotToModuleVelocityVectors(HydraPose robotVel, double trackWidth, double wheelBase) {
        double x = wheelBase / 2;
        double y = trackWidth / 2;

        double vx = robotVel.getX();
        double vy = robotVel.getY();
        double omega = robotVel.getHeading();

        List<HydraVector2d> vectors = new ArrayList<>();
        vectors.add(new HydraVector2d(vx - omega * y, vy + omega * x));
        vectors.add(new HydraVector2d(vx - omega * y, vy - omega * x));
        vectors.add(new HydraVector2d(vx + omega * y, vy - omega * x));
        vectors.add(new HydraVector2d(vx + omega * y, vy + omega * x));

        return vectors;
    }

    /**
     * Computes the wheel velocities corresponding to robotVel given the provided trackWidth and
     * wheelBase.
     *
     * @param robotVel   velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase  distance between pairs of wheels on the same side of the robot
     */
    public static List<Double> robotToWheelVelocities(HydraPose robotVel, double trackWidth, double wheelBase) {
        List<HydraVector2d> moduleVelocities = robotToModuleVelocityVectors(robotVel, trackWidth, wheelBase);
        List<Double> wheelVelocities = new ArrayList<>();

        for (HydraVector2d moduleVelocity : moduleVelocities) {
            wheelVelocities.add(moduleVelocity.norm());
        }

        return wheelVelocities;
    }

    /**
     * Computes the module orientations (in radians) corresponding to robotVel given the provided
     * trackWidth and wheelBase.
     *
     * @param robotVel   velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase  distance between pairs of wheels on the same side of the robot
     */
    public static List<Double> robotToModuleOrientations(HydraPose robotVel, double trackWidth, double wheelBase) {
        List<HydraVector2d> moduleVelocities = robotToModuleVelocityVectors(robotVel, trackWidth, wheelBase);
        List<Double> orientations = new ArrayList<>();

        for (HydraVector2d moduleVelocity : moduleVelocities) {
            orientations.add(moduleVelocity.angle());
        }

        return orientations;
    }

    /**
     * Computes the acceleration vectors corresponding to robotAccel given the provided trackWidth and
     * wheelBase.
     *
     * @param robotAccel velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase  distance between pairs of wheels on the same side of the robot
     */
    public static List<HydraVector2d> robotToModuleAccelerationVectors(HydraPose robotAccel, double trackWidth, double wheelBase) {
        double x = wheelBase / 2;
        double y = trackWidth / 2;

        double ax = robotAccel.getX();
        double ay = robotAccel.getY();
        double alpha = robotAccel.getHeading();

        List<HydraVector2d> vectors = new ArrayList<>();
        vectors.add(new HydraVector2d(ax - alpha * y, ay + alpha * x));
        vectors.add(new HydraVector2d(ax - alpha * y, ay - alpha * x));
        vectors.add(new HydraVector2d(ax + alpha * y, ay - alpha * x));
        vectors.add(new HydraVector2d(ax + alpha * y, ay + alpha * x));

        return vectors;
    }

    /**
     * Computes the wheel accelerations corresponding to robotAccel given the provided trackWidth and
     * wheelBase.
     *
     * @param robotVel    velocity of the robot in its reference frame
     * @param robotAccel  velocity of the robot in its reference frame
     * @param trackWidth  lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase   distance between pairs of wheels on the same side of the robot
     */
    public static List<Double> robotToWheelAccelerations(HydraPose robotVel, HydraPose robotAccel, double trackWidth, double wheelBase) {
        List<HydraVector2d> moduleVelocities = robotToModuleVelocityVectors(robotVel, trackWidth, wheelBase);
        List<HydraVector2d> moduleAccelerations = robotToModuleAccelerationVectors(robotAccel, trackWidth, wheelBase);

        List<Double> wheelAccelerations = new ArrayList<>();
        for (int i = 0; i < moduleVelocities.size(); i++) {
            HydraVector2d vel = moduleVelocities.get(i);
            HydraVector2d accel = moduleAccelerations.get(i);

            wheelAccelerations.add((vel.getX() * accel.getX() + vel.getY() * accel.getY()) / vel.norm());
        }

        return wheelAccelerations;
    }

    /**
     * Computes the module angular velocities corresponding to robotAccel given the provided trackWidth
     * and wheelBase.
     *
     * @param robotVel    velocity of the robot in its reference frame
     * @param robotAccel  velocity of the robot in its reference frame
     * @param trackWidth  lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase   distance between pairs of wheels on the same side of the robot
     */
    public static List<Double> robotToModuleAngularVelocities(HydraPose robotVel, HydraPose robotAccel, double trackWidth, double wheelBase) {
        List<HydraVector2d> moduleVelocities = robotToModuleVelocityVectors(robotVel, trackWidth, wheelBase);
        List<HydraVector2d> moduleAccelerations = robotToModuleAccelerationVectors(robotAccel, trackWidth, wheelBase);

        List<Double> angularVelocities = new ArrayList<>();
        for (int i = 0; i < moduleVelocities.size(); i++) {
            HydraVector2d vel = moduleVelocities.get(i);
            HydraVector2d accel = moduleAccelerations.get(i);

            angularVelocities.add((vel.getX() * accel.getY() - vel.getY() * accel.getX()) / (vel.getX() * vel.getX() + vel.getY() * vel.getY()));
        }

        return angularVelocities;
    }

    /**
     * Computes the robot velocities corresponding to wheelVelocities, moduleOrientations, and the drive parameters.
     *
     * @param wheelVelocities      wheel velocities (or wheel position deltas)
     * @param moduleOrientations   wheel orientations (in radians)
     * @param trackWidth           lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase            distance between pairs of wheels on the same side of the robot
     */
    public static HydraPose wheelToRobotVelocities(List<Double> wheelVelocities, List<Double> moduleOrientations, double trackWidth, double wheelBase) {
        double x = wheelBase / 2;
        double y = trackWidth / 2;

        double vx = 0;
        double vy = 0;
        double frontLeft = 0;
        double rearLeft = 0;
        double rearRight = 0;
        double frontRight = 0;

        for (int i = 0; i < wheelVelocities.size(); i++) {
            double orientation = moduleOrientations.get(i);
            double vel = wheelVelocities.get(i);

            vx += vel * Math.cos(orientation);
            vy += vel * Math.sin(orientation);

            switch (i) {
                case 0:
                    frontLeft = vel;
                    break;
                case 1:
                    rearLeft = vel;
                    break;
                case 2:
                    rearRight = vel;
                    break;
                case 3:
                    frontRight = vel;
                    break;
            }
        }

        double omega = (y * (rearRight + frontRight - frontLeft - rearLeft) + x * (frontLeft + frontRight - rearLeft - rearRight)) / (4 * (x * x + y * y));

        return new HydraPose(vx, vy, omega);
    }
}