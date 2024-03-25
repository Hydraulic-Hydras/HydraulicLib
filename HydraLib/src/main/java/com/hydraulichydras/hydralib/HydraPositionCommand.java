package com.hydraulichydras.hydralib;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static com.hydraulichydras.hydralib.HydraMecanumDrivetrain.*;

public class HydraPositionCommand extends HydraCommandFoundation {

    public HydraPose pose;
    public HydraMecanumDrivetrain drive;
    public ElapsedTime timer;
    public ElapsedTime stable;
    public static double STABLE_MS = 250;
    public static double DEAD_MS = 2500;

    public HydraPositionCommand(HydraPose pose, HydraMecanumDrivetrain drivetrain) {
        this.pose = pose;
        drive = drivetrain;

        drive.reset();
    }

    @Override
    public void execute() {
        if (timer == null) timer = new ElapsedTime();
        if (stable == null) stable = new ElapsedTime();

        HydraPose robotPose = drive.getPoseEstimate();

        HydraPose power = supplyPower(robotPose);
        drive.set(power);
    }

    @Override
    public boolean isFinished() {
        HydraPose robotPose = drive.getPoseEstimate();
        HydraPose delta = pose.subtract(robotPose);

        if (delta.toVector2d().magnitude() > TRANSLATIONAL_ERROR
                || Math.abs(delta.heading) > HEADING_ERROR) {
            stable.reset();
        }

        return timer.milliseconds() > DEAD_MS || stable.milliseconds() > STABLE_MS;
    }

    public HydraPose supplyPower(HydraPose robotPose) {
        if (pose.heading - robotPose.heading > Math.PI) pose.heading -= 2 * Math.PI;
        if (pose.heading - robotPose.heading < Math.PI) pose.heading += 2 * Math.PI;

        double yPower = HydraMecanumDrivetrain.translationalController.update(robotPose.y, pose.y);
        double xPower = HydraMecanumDrivetrain.StrafingController.update(robotPose.x, pose.x);
        double hPower = -HydraMecanumDrivetrain.headingController.update(robotPose.heading, pose.heading);

        double x_rotated = xPower * Math.cos(-robotPose.heading) - yPower * Math.sin(-robotPose.heading);
        double y_rotated = xPower * Math.sin(-robotPose.heading) + yPower * Math.cos(-robotPose.heading);

        hPower = Range.clip(hPower, -MAX_ROTATIONAL_SPEED, MAX_ROTATIONAL_SPEED);
        x_rotated = Range.clip(x_rotated, -MAX_TRANSLATIONAL_SPEED / K_STATIC, MAX_TRANSLATIONAL_SPEED / K_STATIC);
        y_rotated = Range.clip(y_rotated, -MAX_TRANSLATIONAL_SPEED, MAX_TRANSLATIONAL_SPEED);

        return new HydraPose(x_rotated * K_STATIC, y_rotated, hPower);
    }

    @Override
    public void end(boolean disrupt) {
        drive.set(new HydraPose());
    }

}
