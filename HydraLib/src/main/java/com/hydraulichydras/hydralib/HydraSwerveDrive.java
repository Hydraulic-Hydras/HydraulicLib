package com.hydraulichydras.hydralib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class provides the basic functionality of a swerve drive using {@link HydraSwerveKinematics}.
 */
public abstract class HydraSwerveDrive extends HydraDrive {

    private final double kV;
    private final double kA;
    private final double kStatic;
    private final double trackWidth;
    private final double wheelBase;
    public interface Zipper<T, U, R> {
        R zip(T t, U u);
    }

    public static class SwerveLocalizer implements HydraLocalizer {
        private final HydraSwerveDrive drive;
        private final boolean useExternalHeading;
        private HydraPose poseEstimate = new HydraPose();
        private HydraPose poseVelocity = null;
        private List<Double> lastWheelPositions = List.of();
        private double lastExtHeading = Double.NaN;

        public SwerveLocalizer(HydraSwerveDrive drive, boolean useExternalHeading) {
            this.drive = drive;
            this.useExternalHeading = useExternalHeading;
        }

        @Override
        public HydraPose getPoseEstimate() {
            return poseEstimate;
        }

        @Override
        public void setPoseEstimate(HydraPose value) {
            lastWheelPositions = List.of();
            lastExtHeading = Double.NaN;
            if (useExternalHeading) drive.setExternalHeading(value.getHeading());
            poseEstimate = value;
        }

        @Override
        public HydraPose getPoseVelocity() {
            return poseVelocity;
        }
        @Override
        public void update() {
            List<Double> wheelPositions = drive.getWheelPositions();
            List<Double> moduleOrientations = drive.getModuleOrientations();
            double extHeading = useExternalHeading ? drive.getExternalHeading() : Double.NaN;

            if (!lastWheelPositions.isEmpty()) {
                List<Double> wheelDeltas = new ArrayList<>();
                for (int i = 0; i < wheelPositions.size(); i++) {
                    wheelDeltas.add(wheelPositions.get(i) - lastWheelPositions.get(i));
                }

                HydraPose pose = drive.getPoseVelocity();
                List<Double> robotPoseDelta = drive.computeWheelVelocities(pose);

                double finalHeadingDelta = useExternalHeading ?
                        HydraAngle.normDelta(extHeading - lastExtHeading) : robotPoseDelta.get(2);
                poseEstimate = HydraKinematics.relativeOdometryUpdate(poseEstimate,
                        new HydraPose(robotPoseDelta.get(0), robotPoseDelta.get(1), finalHeadingDelta));
            }

            List<Double> wheelVelocities = drive.getWheelVelocities();
            Double extHeadingVel = drive.getExternalHeadingVelocity();

            if (wheelVelocities != null) {
                poseVelocity = HydraSwerveKinematics.wheelToRobotVelocities(
                        wheelVelocities, moduleOrientations, drive.getWheelBase(), drive.getTrackWidth());
                if (useExternalHeading && extHeadingVel != null) {
                    poseVelocity = new HydraPose(poseVelocity.getX(), poseVelocity.getY(), extHeadingVel);
                }
            }

            lastWheelPositions = wheelPositions;
            lastExtHeading = extHeading;
        }

        public static <T, U, R> List<R> zip(List<T> first, List<U> second, Zipper<T, U, R> zipper) {
            return IntStream.range(0, Math.min(first.size(), second.size()))
                    .mapToObj(i -> zipper.zip(first.get(i), second.get(i)))
                    .collect(Collectors.toList());
        }
    }

    protected HydraSwerveDrive(double kV, double kA, double kStatic, double trackWidth, double wheelBase) {
        this.kV = kV;
        this.kA = kA;
        this.kStatic = kStatic;
        this.trackWidth = trackWidth;
        this.wheelBase = wheelBase;
        localizer = new SwerveLocalizer(this, true);
    }

    @Override
    public void setDriveSignal(HydraDriveSignal driveSignal) {
        List<Double> velocities = HydraSwerveKinematics.robotToWheelVelocities(
                driveSignal.getVel(), trackWidth, wheelBase);
        List<Double> orientations = HydraSwerveKinematics.robotToModuleOrientations(
                driveSignal.getVel(), trackWidth, wheelBase);
        List<Double> powers = HydraKinematics.calculateMotorFeedforward(
                velocities, orientations, kV, kA, kStatic);
        setMotorPowers(powers.get(0), powers.get(1), powers.get(2), powers.get(3));
        setModuleOrientations(orientations.get(0), orientations.get(1), orientations.get(2), orientations.get(3));
    }

    @Override
    public void setDrivePower(HydraPose drivePower) {
        double avg = (trackWidth + wheelBase) / 2.0;
        HydraPose pose2d = new HydraPose(drivePower.getX(), drivePower.getY(), drivePower.getHeading());
        List<Double> powers = HydraSwerveKinematics.robotToWheelVelocities(pose2d, trackWidth / avg, wheelBase / avg);
        List<Double> orientations = HydraSwerveKinematics.robotToModuleOrientations(pose2d, trackWidth / avg, wheelBase / avg);
        setMotorPowers(powers.get(0), powers.get(1), powers.get(2), powers.get(3));
        setModuleOrientations(orientations.get(0), orientations.get(1), orientations.get(2), orientations.get(3));
    }

    /**
     * Computes the wheel velocities corresponding to robotVel given the provided trackWidth and
     * wheelBase using SwerveKinematics.
     *
     * @param robotVel   velocity of the robot in its reference frame
     */
    public List<Double> computeWheelVelocities(HydraPose robotVel) {
        return HydraSwerveKinematics.robotToWheelVelocities(robotVel, trackWidth, wheelBase);
    }

    /**
     * Computes the module orientations (in radians) corresponding to robotVel given the provided
     * trackWidth and wheelBase using SwerveKinematics.
     *
     * @param robotVel   velocity of the robot in its reference frame
     */
    public List<Double> computeModuleOrientations(HydraPose robotVel) {
        return HydraSwerveKinematics.robotToModuleOrientations(robotVel, trackWidth, wheelBase);
    }
    public abstract void setModuleOrientations(double frontLeft, double rearLeft, double rearRight, double frontRight);

    public abstract List<Double> getWheelPositions();

    public List<Double> getWheelVelocities() {
        return null;
    }

    public abstract List<Double> getModuleOrientations();

    public abstract void setMotorPowers(double frontLeft, double rearLeft, double rearRight, double frontRight);

    public abstract Double getExternalHeadingVelocity();

    public abstract void setExternalHeading(double heading);

    public Double getWheelBase() {
        return wheelBase;
    }

    public Double getTrackWidth() {
        return trackWidth;
    }

}