package frc.commands.targeting;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

import static frc.utils.Maths.clamp;

public final class AlignToTarget extends PIDCommand {
    @NotNull
    private final DriveTrainSubsystem drive;

    private int wait;

    public AlignToTarget(
            @NotNull PIDController controller,
            @NotNull DriveTrainSubsystem drive
    ) {
        super(
                controller,
                drive.vision::getAlignX,
                0.0,
                (value) -> drive.arcade(
                        0.0,
                        drive.vision.hasTarget() ? clamp(value, 0.4) : 0.0,
                        false
                )
        );
        this.drive = drive;
    }

    @Override
    public void initialize() {
        wait = 0;
    }

    @Override
    public void execute() {
        wait = getController().atSetpoint() ? wait + 1 : 0;
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return wait > 10 && drive.vision.hasTarget();
    }
}
