package frc.commands.intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.subsystems.IntakeSubsystem;
import org.jetbrains.annotations.NotNull;

public class IntakeRetrieve extends InstantCommand {
    @NotNull
    private final IntakeSubsystem intake;

    public IntakeRetrieve(@NotNull IntakeSubsystem intake){
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.retrieve();
    }
}
