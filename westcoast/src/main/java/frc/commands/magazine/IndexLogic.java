package frc.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public final class IndexLogic extends CommandBase {
    @NotNull
    private final MagazineSubsystem magazine;

    private double boost = 0.0;

    public IndexLogic(@NotNull MagazineSubsystem magazine) {
        this.magazine = magazine;
        addRequirements(magazine);
    }

    @Override
    public void initialize() {
        boost = 0.019 * magazine.getIndexCount();
    }

    @Override
    public void execute() {
        switch (magazine.getSensorSwitch()) {
            case 0x0:
                magazine.runIndexMotor(0.3);
                magazine.runMagMotor(0.3 + boost); //No sensors see anything.
                break;
            case 0x1:
                magazine.runIndexMotor(0.3);
                magazine.runMagMotor(0.43); //Intake sensor sees something.
                break;
            case 0x2:
                break; //Index sensor sees something.
            case 0x3:
                magazine.runIndexMotor(0.27); //Both Index sensor and Intake sensor see something (all balls after first)
                magazine.runMagMotor(0.3 + boost); //Boosts magazines speed so as to avoid magazine still seeing ball while index get's cleared (could also be fixed by moving sensor positions)
                break;
            case 0x4: //End sensor sees something (shouldn't happen).
            case 0x5: //Intake sensor and End sensor see something (shouldn't happen).
            case 0x6: //End sensor and Index sensor sees something (happens when 4 balls are in the system).
            case 0x7: //All sensors are saturated (happens with 5 balls).
            default:
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Index Complete");
        magazine.stopMotors();
    }

    @Override
    public boolean isFinished() {
        switch (magazine.getSensorSwitch()) {
            case 0x0:
            case 0x1:
            case 0x3:
                return false;
            case 0x2:
            case 0x4:
            case 0x5:
            case 0x6:
            case 0x7:
            default:
                return true;
        }
    }
}
