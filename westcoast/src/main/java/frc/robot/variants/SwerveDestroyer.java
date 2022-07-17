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
import frc.robot.definition.Motors.ClimberMotors;
import frc.robot.definition.Motors.IntakeMotors;
import frc.robot.definition.Motors.MagazineMotors;
import frc.robot.definition.Motors.ShooterMotors;
import frc.robot.definition.Sensors.DriveSensors;
import frc.robot.definition.Sensors.MagazineSensors;
import frc.vision.LimelightRunner;
import org.jetbrains.annotations.NotNull;

import static frc.robot.definition.Motors.SwerveCTRMotors;
import static frc.robot.definition.Motors.driveCTRMotors;
import static frc.robot.definition.Ownership.parameter;

public interface SwerveDestroyer {
    @NotNull
    static Definition definition() {
        return Definition.definition(
                "Swerve Destroyer",
                new Input(
                        new Joystick(0)
                ),
                new Motors(null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        SwerveCTRMotors(
                                new WPI_TalonSRX(5),
                                new WPI_TalonSRX(14)
                        ) //test purposes, will definitely need more motors in a robot
                ),
                null,
                null,
                null,
                null
        );
    }
    private static Encoder encoder(int channelA, int channelB) {
        final Encoder encoder = new Encoder(channelA, channelB);
        encoder.setDistancePerPulse(0.0018868); // 530 pulses = 1 m
        return encoder;
    }
}
