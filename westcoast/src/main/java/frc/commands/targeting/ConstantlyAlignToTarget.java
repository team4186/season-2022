package frc.commands.targeting;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

import static frc.utils.Maths.clamp;

public final class ConstantlyAlignToTarget extends CommandBase {
    @NotNull
    private final PIDController turn;
    @NotNull
    private final PIDController forward;
    @NotNull
    private final DriveTrainSubsystem drive;

    public ConstantlyAlignToTarget(
            @NotNull PIDController turn,
            @NotNull PIDController forward,
            @NotNull DriveTrainSubsystem drive
    ) {
        this.turn = turn;
        this.forward = forward;
        this.drive = drive;
    }

    @Override
    public void initialize() {
        turn.reset();
        forward.reset();
    }

    @Override
    public void execute() {
        drive.arcade(
                clamp(-forward.calculate(drive.vision.getDistance(), 5.0), 0.4),
                clamp(turn.calculate(drive.vision.getAlignX(), 0.0), 0.4),
                false
        );
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }
}
