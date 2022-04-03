package frc.commands.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

import static java.lang.Math.copySign;

public class TeleopDrive extends CommandBase {
    @NotNull
    private final DoubleSupplier xAxis;
    @NotNull
    private final DoubleSupplier yAxis;
    @NotNull
    private final Button attenuate;
    @NotNull
    private final Button invert;
    @NotNull
    private final DriveTrainSubsystem drive;

    private double forward = 1;

    private boolean shouldAttenuate;

    public TeleopDrive(
            @NotNull DoubleSupplier xAxis,
            @NotNull DoubleSupplier yAxis,
            @NotNull Button attenuate,
            @NotNull Button invert,
            @NotNull DriveTrainSubsystem drive
    ) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
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
            forward = attenuated(this.forward * yAxis.getAsDouble());
            zRotation = attenuated(-xAxis.getAsDouble());
        } else {
            forward = full(this.forward * yAxis.getAsDouble());
            zRotation = MathUtil.clamp(full(-xAxis.getAsDouble()), -0.5, 0.5);
        }
        drive.arcade(-forward, -zRotation, false);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    private double full(double value) {
        return copySign(Math.pow(Math.abs(value), 1.6), value);
    }

    private double attenuated(double value) {
        return 0.5 * value;
    }
}