package frc.commands.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

public final class EncoderDrive extends CommandBase {
    @NotNull
    private final DriveTrainSubsystem drive;
    @NotNull
    private final Joystick joystick;
    @NotNull
    private final Button invert;
    @NotNull
    private final PIDController left;
    @NotNull
    private final PIDController right;
    @NotNull
    private final Button turnInPlace;

    private static final double maxSpeed = 7 * 256; // encoders report ~1550-1600 pulses

    private double shouldInvert = 1.0;

    private final double[] leftData = {0.0, 0.0};
    private final double[] rightData = {0.0, 0.0};

    public EncoderDrive(
            @NotNull Joystick joystick,
            @NotNull Button invert,
            @NotNull Button turnInPlace,
            @NotNull DriveTrainSubsystem drive
    ) {
        this.joystick = joystick;
        this.invert = invert;
        this.turnInPlace = turnInPlace;
        this.drive = drive;

        double P = 0.002;
        double I = 0.0;
        double D = 0.0;

        left = new PIDController(P, I, D);
        left.setTolerance(1, 1);
        left.disableContinuousInput();

        right = new PIDController(P, I, D);
        right.setTolerance(1, 1);
        right.disableContinuousInput();

        addRequirements(drive);

        SmartDashboard.putNumber("P", P);
        SmartDashboard.putNumber("I", I);
        SmartDashboard.putNumber("D", D);
    }

    @Override
    public void initialize() {
        drive.leftEncoder.reset();
        drive.rightEncoder.reset();

        right.reset();
        left.reset();

        invert.whenPressed(() -> shouldInvert *= -1);
    }

    @Override
    public void execute() {
        // region Tuning

        double P = SmartDashboard.getNumber("P", left.getP());
        double I = SmartDashboard.getNumber("I", left.getI());
        double D = SmartDashboard.getNumber("D", left.getD());

        left.setPID(P, I, D);
        right.setPID(P, I, D);

        // endregion


        // region Input

        double forward = MathUtil.clamp(shouldInvert * -joystick.getY(), -1.0, 1.0);
        double turn = MathUtil.clamp(joystick.getX(), -1.0, 1.0);

        // endregion


        // region Input -> Output

        turn = Math.copySign(turn * turn, turn);

        double leftSpeed;
        double rightSpeed;

        if (turnInPlace.get()) {
            forward = Math.copySign(forward * forward, forward);
            leftSpeed = forward + turn * 0.5;
            rightSpeed = forward - turn * 0.5;
        } else {
            leftSpeed = forward + forward * turn;
            rightSpeed = forward - (forward * turn);
        }

        // Normalize wheel speeds
        double maxMagnitude = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
        if (maxMagnitude > 1.0) {
            leftSpeed /= maxMagnitude;
            rightSpeed /= maxMagnitude;
        }

        // endregion


        // region Feed the PIDs

        double leftSetpoint = leftSpeed * maxSpeed;
        double rightSetpoint = rightSpeed * maxSpeed;

        double leftOut = left.calculate(drive.leftEncoder.getRate(), leftSetpoint);
        double rightOut = right.calculate(drive.rightEncoder.getRate(), rightSetpoint);

        //endregion


        // region Output

        drive.setMotorOutput(leftOut, rightOut);

        // endregion


        // region Debug

        leftData[0] = drive.leftEncoder.getRate();
        leftData[1] = leftSetpoint;
        rightData[0] = drive.rightEncoder.getRate();
        rightData[1] = rightSetpoint;

        SmartDashboard.putBoolean("Revert", shouldInvert > 0.0);
        SmartDashboard.putNumber("Forward", shouldInvert * forward);
        SmartDashboard.putNumber("Turn", turn);

        SmartDashboard.putNumberArray("lE Rate", leftData);
        SmartDashboard.putNumber("lOutput", leftOut);

        SmartDashboard.putNumberArray("rE Rate", rightData);
        SmartDashboard.putNumber("rOutput", rightOut);

        // endregion
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }
}
