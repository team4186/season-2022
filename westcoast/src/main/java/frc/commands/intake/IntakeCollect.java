package frc.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.IntakeSubsystem;
import org.jetbrains.annotations.NotNull;

public class IntakeCollect extends CommandBase {
    @NotNull
    private final IntakeSubsystem intake;

    public IntakeCollect(@NotNull IntakeSubsystem intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.start();
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }
}
