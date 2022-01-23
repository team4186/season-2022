package frc.robot.definition;

import frc.subsystems.DriveTrainSubsystem;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public class Subsystems {
    @NotNull
    public final DriveTrainSubsystem driveTrain;
    @NotNull
    public final MagazineSubsystem magazine;

    public Subsystems(@NotNull DriveTrainSubsystem driveTrain, @NotNull MagazineSubsystem magazine) {
        this.driveTrain = driveTrain;
        this.magazine = magazine;
    }
}