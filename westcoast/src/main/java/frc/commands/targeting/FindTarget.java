package frc.commands.targeting;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;


public final class FindTarget extends CommandBase {
    @NotNull
    private final DriveTrainSubsystem drive;

    public FindTarget(@NotNull DriveTrainSubsystem drive) {
        this.drive = drive;
    }

    @Override
    public void execute() {
        drive.tank(-0.35, 0.35, false);
    }

    @Override
    public boolean isFinished() {
        return drive.vision.hasTarget();
    }
}
