package frc.robot.variants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import frc.robot.definition.*;
import frc.robot.definition.Motors.MagazineMotors;
import frc.robot.definition.Motors.ShooterMotors;
import frc.robot.definition.Sensors.DriveSensors;
import frc.robot.definition.Sensors.MagazineSensors;
import frc.vision.LimelightRunner;
import org.jetbrains.annotations.NotNull;

import static frc.robot.definition.Motors.driveCTRMotors;
import static frc.robot.definition.Ownership.parameter;

public interface ShinDestroyer {
    @NotNull
    static Definition definition() {
        return Definition.definition(
                "Shin Destroyer",
                new Input(
                        1.0,
                        new Joystick(0)
                ),
                new Motors(
                        driveCTRMotors(
                                new WPI_TalonSRX(14),
                                new WPI_VictorSPX(13),
                                new WPI_VictorSPX(15),
                                false
                        ),
                        driveCTRMotors(
                                new WPI_TalonSRX(2),
                                new WPI_VictorSPX(1),
                                new WPI_VictorSPX(3),
                                true
                        ),
                        new MagazineMotors(
                                new WPI_VictorSPX(7),
                                new VictorSP(10),
                                new VictorSP(12)
                        ),
                        new ShooterMotors(
                                new WPI_TalonSRX(8),
                                new WPI_TalonSRX(9)
                        )
                ),
                new Sensors(
                        new DriveSensors(
                                new AHRS(SPI.Port.kMXP),
                                encoder(9, 8),
                                encoder(6, 7),
                                new LimelightRunner()
                        ),
                        new MagazineSensors(
                                new DigitalInput(0),
                                new DigitalInput(1),
                                new DigitalInput(2)
                        )
                ),
                new Parameters(
                        parameter(62.0),
                        parameter(1.04)
                ),
                new Controllers() {
                    @Override
                    public PIDController gyroDrive() {
                        final PIDController controller = new PIDController(0.4, 0.12, 0.01);
                        controller.setTolerance(0.5);
                        controller.disableContinuousInput();
                        return controller;
                    }

                    @Override
                    public ProfiledPIDController leaveLine() {
                        final ProfiledPIDController controller = new ProfiledPIDController(
                                0.02,
                                0.0,
                                0.001,
                                new TrapezoidProfile.Constraints(1000.0, 750.0)
                        );

                        controller.setTolerance(5.0, 100.0);
                        controller.disableContinuousInput();
                        return controller;
                    }

                    @Override
                    public ProfiledPIDController perfectTurn() {
                        final ProfiledPIDController controller = new ProfiledPIDController(
                                0.05,
                                0.0,
                                0.0,
                                new TrapezoidProfile.Constraints(500.0, 300.0)
                        );

                        controller.setTolerance(5.0, 50.0);
                        controller.disableContinuousInput();
                        return controller;
                    }

                    @Override
                    public PIDController alignToTarget() {
                        final PIDController controller = new PIDController(0.7, 0.1, 0.07);
                        controller.disableContinuousInput();
                        controller.setTolerance(0.1);
                        return controller;
                    }

                    @Override
                    public PIDController stayOnTarget() {
                        final PIDController controller = new PIDController(0.1, 0.0, 0.0);
                        controller.disableContinuousInput();
                        controller.setTolerance(0.2);
                        return controller;
                    }
                }
        );
    }

    private static Encoder encoder(int channelA, int channelB) {
        final Encoder encoder = new Encoder(channelA, channelB);
        encoder.setDistancePerPulse(0.390625);
        return encoder;
    }
}
