package frc.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.MagazineSubsystem;
import frc.subsystems.ShooterSubsystem;
import org.jetbrains.annotations.NotNull;

public final class Shoot extends CommandBase {
    @NotNull
    private final ShooterSubsystem shooter;
    @NotNull
    private final MagazineSubsystem magazine;

    public Shoot(
            @NotNull ShooterSubsystem shooter,
            @NotNull MagazineSubsystem magazine
    ) {
        this.shooter = shooter;
        this.magazine = magazine;
        addRequirements(shooter, magazine);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setSpeed(0.5);
        magazine.startFeederMotor();
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
        magazine.stopAll();
    }
}
