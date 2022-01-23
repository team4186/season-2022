package frc.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.subsystems.DriveTrainSubsystem;
import frc.utils.JoystickUtils;
import frc.utils.PidUtils;
import org.jetbrains.annotations.NotNull;

import static frc.utils.Maths.attenuate;

public class GyroDrive extends PIDCommand {
    @NotNull
    private final DriveTrainSubsystem drive;

    public GyroDrive(
            final double forward,
            @NotNull PIDController controller,
            @NotNull Joystick joystick,
            @NotNull DriveTrainSubsystem drive
    ) {
        super(
                controller,
                () -> PidUtils.clean(drive.gyro.getRate(), 6.0, 0.3),
                () -> JoystickUtils.clean((forward * joystick.getX()), 0.05) * 4.6,
                (value) -> drive.arcade(
                        attenuate(forward * joystick.getY()),
                        PidUtils.clean(-value, 0.8, 0.05),
                        false
                ),
                drive
        );

        this.drive = drive;
    }

    @Override
    public void initialize() {
        drive.gyro.reset();
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }
}