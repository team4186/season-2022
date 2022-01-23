package frc.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public final class ShooterAccelerator extends CommandBase {
    @NotNull
    private final MagazineSubsystem magazine;

    public ShooterAccelerator(@NotNull MagazineSubsystem magazine) {
        this.magazine = magazine;
        addRequirements(magazine);
    }


    @Override
    public void initialize() {
        magazine.shooterTune();
    }

    @Override
    public void execute() {
        magazine.runShooter(0.78);
        magazine.publishCurrentLevels();
    }

    @Override
    public void end(boolean interrupted) {
        magazine.stopMotors();
    }
}
