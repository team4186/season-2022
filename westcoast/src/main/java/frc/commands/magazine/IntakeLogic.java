package frc.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public final class IntakeLogic extends CommandBase {
    @NotNull
    private final MagazineSubsystem magazine;

    public IntakeLogic(@NotNull MagazineSubsystem magazine) {
        this.magazine = magazine;
        addRequirements(magazine);
    }

    @Override
    public void execute() {
        switch (magazine.getSensorSwitch()) {
            case 0x0: //No sensors see anything.
            case 0x2: //Index sensor sees something.
            case 0x5: //Intake sensor and End sensor see something (shouldn't happen).
            case 0x6: //End sensor and Index sensor sees something (happens when 4 balls are in the system).
                magazine.runSyncIntdex(0.4);
                break;
            case 0x1: //Intake sensor sees something.
            case 0x3: //Both Index sensor and Intake sensor see something (all balls after first)
            case 0x4: //End sensor sees something (shouldn't happen).
            case 0x7: //All sensors are saturated (happens with 5 balls).
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        magazine.stopMotors();
        magazine.incrementIndexCount();
    }

    @Override
    public boolean isFinished() {
        switch (magazine.getSensorSwitch()) {
            case 0x0:
            case 0x2:
            case 0x5:
            case 0x6:
                return false;
            case 0x1:
            case 0x3:
            case 0x4:
            case 0x7:
            default:
                return true;
        }
    }
}
