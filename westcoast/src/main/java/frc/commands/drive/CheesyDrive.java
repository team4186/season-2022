package frc.commands.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class CheesyDrive extends CommandBase {
    @NotNull
    private final DoubleSupplier yaw;
    @NotNull
    private final DoubleSupplier throttle;
    @NotNull
    private final BooleanSupplier isHighGear;
    @NotNull
    private final BooleanSupplier isQuickTurn;
    @NotNull
    private final BooleanSupplier invert;
    @NotNull
    private final DriveTrainSubsystem drive;

    private final double sensitivityHigh;
    private final double sensitivityLow;

    public CheesyDrive(
            @NotNull DoubleSupplier yaw,
            @NotNull DoubleSupplier throttle,
            @NotNull BooleanSupplier isHighGear,
            @NotNull BooleanSupplier isQuickTurn,
            @NotNull BooleanSupplier invert,
            @NotNull DriveTrainSubsystem drive,
            double sensitivityHigh,
            double sensitivityLow
    ) {
        this.yaw = yaw;
        this.throttle = throttle;
        this.isHighGear = isHighGear;
        this.isQuickTurn = isQuickTurn;
        this.invert = invert;
        this.drive = drive;
        this.sensitivityHigh = sensitivityHigh;
        this.sensitivityLow = sensitivityLow;
    }


    private double oldWheel = 0.0;
    private double quickStopAccumulator = 0.0;

    @Override
    public void execute() {
        double wheelNonLinearity;
        // NOTE do we need a deadband?
//        double wheel = handleDeadband(getWheel(stick), C.Drive.wheelDeadband);
        double wheel = -yaw.getAsDouble();

        // NOTE do we need a deadband?
//        double throttle = -handleDeadband(getThrottle(stick), C.Drive.throttleDeadband);
        double throttle = this.throttle.getAsDouble() * (invert.getAsBoolean() ? 1.0 : -1.0);

        double negInertia = wheel - oldWheel;

        /*
         * if(getAverageSpeed()> 2000){ SetHighGear(); } else if (getAverageSpeed() <
         * 1500){ SetLowGear(); } //Autoshifting code based on a speed threshold from encoder data (not implemented)
         */

        oldWheel = wheel;
        // Apply a sin function that's scaled to make it feel better. WPILib does similar thing by squaring inputs.
        if (isHighGear.getAsBoolean()) {
            wheelNonLinearity = 0.6;
        } else {
            wheelNonLinearity = 0.5;
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        }
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);

        double leftPwm, rightPwm, overPower;
        double sensitivity;
        double angularPower;
        double linearPower;
        // Negative inertia!
        double negInertiaAccumulator = 0.0;
        double negInertiaScalar;

        if (isHighGear.getAsBoolean()) {
            negInertiaScalar = 5.0;
            sensitivity = sensitivityHigh;
        } else {
            if (wheel * negInertia > 0) {
                negInertiaScalar = 2.5;
            } else {
                if (Math.abs(wheel) > 0.65) {
                    negInertiaScalar = 5.0;
                } else {
                    negInertiaScalar = 3.0;
                }
            }
            sensitivity = sensitivityLow;
            // useless code?
//            if (Math.abs(throttle) > 0.1) {
            // sensitivity = 1.0 - (1.0 - sensitivity) / Math.abs(throttle);
//            }
        }

        double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccumulator += negInertiaPower;
        wheel = wheel + negInertiaAccumulator;
        // NOTE useless code?
//        if (negInertiaAccumulator > 1) {
//            negInertiaAccumulator -= 1;
//        } else if (negInertiaAccumulator < -1) {
//            negInertiaAccumulator += 1;
//        } else {
//            negInertiaAccumulator = 0;
//        }
        linearPower = throttle;
        // Quickturn!
        if (isQuickTurn.getAsBoolean()) {
            if (Math.abs(linearPower) < 0.2) {
                double alpha = 0.1;
                quickStopAccumulator = (1.0 - alpha) * quickStopAccumulator + alpha * MathUtil.clamp(wheel, -1.0, 1.0) * 5;
            }
            overPower = 1.0;
            // NOTE useless code?
//            if (isHighGear.getAsBoolean()) {
//                sensitivity = .005;
//            } else {
//                sensitivity = 0.005;
//
//            }
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }
        rightPwm = leftPwm = linearPower;
        leftPwm -= angularPower;
        rightPwm += angularPower;
        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }

        drive.setMotorOutput(leftPwm, rightPwm);
    }
}
