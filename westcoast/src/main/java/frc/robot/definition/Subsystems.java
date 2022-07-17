package frc.robot.definition;

import frc.subsystems.*;
import org.jetbrains.annotations.NotNull;

public class Subsystems {
    @NotNull
    public final DriveTrainSubsystem driveTrain;
    @NotNull
    public final IntakeSubsystem intake;
    @NotNull
    public final ShooterSubsystem shooter;
    @NotNull
    public final MagazineSubsystem magazine;
    @NotNull
    public final ClimberSubsystem climber;
    @NotNull
    public final SwerveDriveSubsystem swerveDrive;

    public Subsystems(
            @NotNull DriveTrainSubsystem driveTrain,
            @NotNull IntakeSubsystem intake,
            @NotNull ShooterSubsystem shooter,
            @NotNull MagazineSubsystem magazine,
            @NotNull ClimberSubsystem climber,
            @NotNull SwerveDriveSubsystem swerveDrive
    ) {
        this.driveTrain = driveTrain;
        this.intake = intake;
        this.shooter = shooter;
        this.magazine = magazine;
        this.climber = climber;
        this.swerveDrive = swerveDrive;
    }
}