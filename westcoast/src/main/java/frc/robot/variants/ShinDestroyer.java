package frc.robot.variants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.definition.*;
import frc.robot.definition.Motors.IntakeMotors;
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
                        new Joystick(0)
                ),
                new Motors(
                        driveCTRMotors(
                                new WPI_TalonSRX(5),
                                new WPI_VictorSPX(3),
                                new WPI_VictorSPX(4),
                                true
                        ),
                        driveCTRMotors(
                                new WPI_TalonSRX(2),
                                new WPI_VictorSPX(6),
                                new WPI_VictorSPX(7),
                                false
                        ),
                        new IntakeMotors(
                                new CANSparkMax(13, CANSparkMaxLowLevel.MotorType.kBrushless)
                        ),
                        new MagazineMotors(
                                new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless),
                                new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless),
                                new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless)
                        ),
                        new ShooterMotors(
                                new CANSparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless),
                                new CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless)
                        )
                ),
                new Pneumatics(
                        new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 1)
                ),
                new Sensors(
                        new DriveSensors(
                                dummyGyro(),
                                encoder(8, 9),
                                encoder(7, 6),
                                new LimelightRunner()
                        ),
                        new MagazineSensors(
                                new DigitalInput(2),
                                new DigitalInput(1),
                                new DigitalInput(0),
                                new ColorSensorV3(I2C.Port.kOnboard)
                        )
                ),
                new Parameters(
                        parameter(1.04)
                ),
                new Controllers() {
                    @NotNull
                    @Override
                    public PIDController gyroDrive() {
                        final PIDController controller = new PIDController(0.4, 0.12, 0.01);
                        controller.setTolerance(0.5);
                        controller.disableContinuousInput();
                        return controller;
                    }

                    @NotNull
                    @Override
                    public ProfiledPIDController leaveLine() {
                        final ProfiledPIDController controller = new ProfiledPIDController(
                                0.01,
                                0.0,
                                0.001,
                                new TrapezoidProfile.Constraints(1000.0, 750.0)
                        );

                        controller.setTolerance(5.0, 100.0);
                        controller.disableContinuousInput();
                        return controller;
                    }

                    @NotNull
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

                    @NotNull
                    @Override
                    public PIDController alignToTarget() {
                        final PIDController controller = new PIDController(0.7, 0.1, 0.07);
                        controller.disableContinuousInput();
                        controller.setTolerance(0.1);
                        return controller;
                    }

                    @NotNull
                    @Override
                    public PIDController stayOnTarget() {
                        final PIDController controller = new PIDController(0.1, 0.0, 0.0);
                        controller.disableContinuousInput();
                        controller.setTolerance(0.2);
                        return controller;
                    }

                    @Override
                    public void shooterConfig(@NotNull SparkMaxPIDController controller) {
                        controller.setP(0.00015);
                        controller.setI(0.0);
                        controller.setIZone(0.0);
                        controller.setD(0.0007);
                        controller.setFF(0.0002);
                        controller.setOutputRange(0.0, 1.0);
                    }
                }
        );
    }

    @NotNull
    private static Gyro dummyGyro() {
        return new Gyro() {
            @Override
            public void calibrate() {

            }

            @Override
            public void reset() {

            }

            @Override
            public double getAngle() {
                return 0;
            }

            @Override
            public double getRate() {
                return 0;
            }

            @Override
            public void close() {

            }
        };
    }

    private static Encoder encoder(int channelA, int channelB) {
        final Encoder encoder = new Encoder(channelA, channelB);
        encoder.setDistancePerPulse(0.390625); // 800 pulses = 5 ft,
        return encoder;
    }
}
