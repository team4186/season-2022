package frc.commands.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.DriveTrainSubsystem;

public final class EncoderDrive extends CommandBase {
    private final DriveTrainSubsystem drive;
    private final Joystick joystick;
    private final PIDController left;
    private final PIDController right;
    private final double maxSpeed = 1024; // encoders report ~1550-1600 pulses

    private final double[] leftData = {0.0, 0.0};
    private final double[] rightData = {0.0, 0.0};

    public EncoderDrive(
            DriveTrainSubsystem drive,
            Joystick joystick
    ) {
        this.drive = drive;
        this.joystick = joystick;

        double P = 0.0024;
        double I = 0.0;
        double D = 0.0;

        left = new PIDController(P, I, D);
        left.setTolerance(1, 1);
        left.disableContinuousInput();

        right = new PIDController(P, I, D);
        right.setTolerance(1, 1);
        right.disableContinuousInput();

        SmartDashboard.putNumber("P", P);
        SmartDashboard.putNumber("I", I);
        SmartDashboard.putNumber("D", D);
        SmartDashboard.putNumber("Forward", 0);
    }

    @Override
    public void initialize() {
        drive.leftEncoder.reset();
        drive.rightEncoder.reset();

        right.reset();
        left.reset();
    }

    @Override
    public void execute() {
        double P = SmartDashboard.getNumber("P", 0.0024);
        double I = SmartDashboard.getNumber("I", 0);
        double D = SmartDashboard.getNumber("D", 0);

        left.setPID(P, I, D);
        right.setPID(P, I, D);

        double forward = MathUtil.clamp(joystick.getY(), -1.0, 1.0);
        double turn = MathUtil.clamp(joystick.getX(), -1.0, 1.0);

        double leftSpeed;
        double rightSpeed;

        if (false && forward > -0.05 && forward < 0.05) {
            leftSpeed = forward + turn;
            rightSpeed = forward - turn;
        } else {
            leftSpeed = forward + Math.abs(forward) * turn;
            rightSpeed = forward - Math.abs(forward) * turn;
        }

        // Normalize wheel speeds
        double maxMagnitude = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
        if (maxMagnitude > 1.0) {
            leftSpeed /= maxMagnitude;
            rightSpeed /= maxMagnitude;
        }

        double leftSetpoint = leftSpeed * maxSpeed;
        double rightSetpoint = rightSpeed * maxSpeed;

        double leftOut = left.calculate(drive.leftEncoder.getRate(), leftSetpoint);
        double rightOut = right.calculate(drive.rightEncoder.getRate(), rightSetpoint);

        // TODO replace with direct motor call or a DriveTrainSubsystem.setSpeeds
        drive.tank(-leftOut, -rightOut, false);

        leftData[0] = drive.leftEncoder.getRate();
        leftData[1] = leftSetpoint;
        rightData[0] = drive.rightEncoder.getRate();
        rightData[1] = rightSetpoint;

        SmartDashboard.putNumberArray("lE Rate", leftData);
        SmartDashboard.putNumber("lOutput", leftOut);

        SmartDashboard.putNumberArray("rE Rate", rightData);
        SmartDashboard.putNumber("rOutput", rightOut);
    }

    @Override
    public void end(boolean interrupted) {
        drive.leftEncoder.reset();
        drive.rightEncoder.reset();

        drive.stop();
        left.reset();
        right.reset();
    }
}
