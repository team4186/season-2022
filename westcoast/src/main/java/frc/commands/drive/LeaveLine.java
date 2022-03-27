package frc.commands.drive;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

import static edu.wpi.first.math.MathUtil.clamp;

public class LeaveLine extends CommandBase {
    private final double distance;
    @NotNull
    private final ProfiledPIDController left;
    @NotNull
    private final ProfiledPIDController right;
    @NotNull
    private final DriveTrainSubsystem drive;

    private int wait = 0;

    public LeaveLine(
            double distance,
            @NotNull ProfiledPIDController left,
            @NotNull ProfiledPIDController right,
            @NotNull DriveTrainSubsystem drive
    ) {
        this.distance = distance;
        this.left = left;
        this.right = right;
        this.drive = drive;
    }

    @Override
    public void initialize() {
        wait = 0;
        drive.rightEncoder.reset();
        drive.leftEncoder.reset();
        right.reset(0);
        left.reset(0);
    }

    @Override
    public void execute() {
        double rightOut = clamp(right.calculate(drive.rightEncoder.getDistance(), distance), -0.4, 0.4);
        double leftOut = clamp(left.calculate(drive.leftEncoder.getDistance(), distance), -0.4, 0.4);
        drive.setMotorOutput(leftOut, rightOut);
        wait = (right.atGoal() && left.atGoal()) ? wait + 1 : 0;
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return wait >= 10;
    }
}