package com.hydraulichydras.hydralib.Util;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.hydraulichydras.hydralib.Geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Arrays;
import java.util.List;

public class ThreeWheelOdom extends ThreeTrackingWheelLocalizer implements Localizer {

    public double TICKS_PER_REV;
    public double WHEEL_RADIUS;
    public double GEAR_RATIO;
    public Encoder leftEncoder, rightEncoder, frontEncoder;
    public static HardwareMap hardwareMap;
    public double X_MULTIPLIER, Y_MULTIPLIER;

    public ThreeWheelOdom(String LeftEnc, String RightEnc, String FrontEnc, double LATERAL_DISTANCE, double FORWARD_OFFSET) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, LeftEnc));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, RightEnc));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, FrontEnc));
    }
    public void setMultiplier(double X, double Y) {
        this.X_MULTIPLIER = X;
        this.Y_MULTIPLIER = Y;
    }
    public void setConstraints(double TICKS_PER_REV, double WHEEL_RADIUS, double GEAR_RATIO) {
        this.TICKS_PER_REV = TICKS_PER_REV;
        this.WHEEL_RADIUS = WHEEL_RADIUS;
        this.GEAR_RATIO = GEAR_RATIO;
    }
    public double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }
    public void setDirections(Encoder.Direction leftDirection, Encoder.Direction rightDirection,Encoder.Direction frontDirection) {
        leftEncoder.setDirection(leftDirection);
        rightEncoder.setDirection(rightDirection);
        frontEncoder.setDirection(frontDirection);
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition() * X_MULTIPLIER),
                encoderTicksToInches(rightEncoder.getCurrentPosition() * X_MULTIPLIER),
                encoderTicksToInches(frontEncoder.getCurrentPosition() * Y_MULTIPLIER)
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(0.0, 0.0, 0.0);
    }

    @Override
    public void periodic() {
        super.update();
    }

    @Override
    public Pose getPos() {
        Pose2d pose = getPoseEstimate();
        return new Pose(-pose.getY(), pose.getX(), pose.getHeading());
    }

    @Override
    public void setPos(Pose pose) {
        super.setPoseEstimate(new Pose2d(pose.y, -pose.x, pose.heading));
    }
}
