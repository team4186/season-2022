package frc.commands.targeting;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

import static frc.utils.Maths.clamp;

public final class StayOnTarget extends PIDCommand {
    @NotNull
    private final DriveTrainSubsystem drive;

    public StayOnTarget(
            @NotNull PIDController controller,
            @NotNull DriveTrainSubsystem drive
    ) {
        super(
                controller,
                drive.vision::getXOffset,
                0.0,
                (value) -> drive.arcade(0.0, clamp(value, 0.4), false),
                drive
        );
        this.drive = drive;
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }
}
