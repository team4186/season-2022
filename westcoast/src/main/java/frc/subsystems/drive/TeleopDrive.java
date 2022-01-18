package frc.subsystems.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TeleopDrive extends CommandBase {
    private final DifferentialDrive drive;
    private final Joystick joy;
    private double direction;

    public TeleopDrive (DifferentialDrive drive, Joystick joy) {
        this.drive = drive;
        this.joy = joy;
    }

    @Override
    public void initialize () {
        direction = 1.0;
    }

    @Override
    public void execute() {
        drive.arcadeDrive(direction * joy.getY(), -direction * joy.getX());
    }

    @Override
    public void end(boolean interrupted) {
        drive.stopMotor();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
