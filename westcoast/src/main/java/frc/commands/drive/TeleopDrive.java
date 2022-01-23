package frc.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.copySign;

public class TeleopDrive extends CommandBase {
    @NotNull
    private final Joystick joystick;
    @NotNull
    private final Button attenuate;
    @NotNull
    private final Button invert;
    @NotNull
    private final DriveTrainSubsystem drive;

    private double forward = 1;

    private boolean shouldAttenuate;

    public TeleopDrive(
            @NotNull Joystick joystick,
            @NotNull Button attenuate,
            @NotNull Button invert,
            @NotNull DriveTrainSubsystem drive
    ) {
        this.joystick = joystick;
        this.attenuate = attenuate;
        this.invert = invert;
        this.drive = drive;

        addRequirements(drive);
    }

    @Override
    public void initialize() {
        attenuate
                .whenPressed(() -> shouldAttenuate = true)
                .whenReleased(() -> shouldAttenuate = false);
        invert.whenPressed(() -> forward *= -1);
    }

    @Override
    public void execute() {
        final double forward;
        final double zRotation;
        if (shouldAttenuate) {
            forward = attenuated(this.forward * joystick.getY());
            zRotation = attenuated(-joystick.getX());
        } else {
            forward = full(this.forward * joystick.getY());
            zRotation = full(-joystick.getX());
        }
        drive.arcade(forward, zRotation, false);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    private double full(double value) {
        return copySign(value * value, value);
    }

    private double attenuated(double value) {
        return 0.5 * value;
    }
}