package frc.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public final class EverythingOut extends CommandBase {
    @NotNull
    private final MagazineSubsystem magazine;

    public EverythingOut(@NotNull MagazineSubsystem magazine) {
        this.magazine = magazine;
        addRequirements(magazine);
    }

    @Override
    public void initialize() {
        magazine.resetIndexCount();
    }

    @Override
    public void execute() {
        magazine.runIntakeMotor(-0.4);
        magazine.runIndexMotor(-0.4);
        magazine.runMagMotor(-0.4);
    }

    @Override
    public void end(boolean interrupted) {
        magazine.stopMotors();
    }
}
