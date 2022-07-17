package frc.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.vision.VisionRunner;
import org.jetbrains.annotations.NotNull;

// MOTOR LAYOUT
/*
 *                  Front
 *
 *      Motors 2 ------------ Motors 1
 *          |                   |
 *          |                   |
 *  Left    |       Robot       |    Right
 *          |                   |
 *          |                   |
 *      Motors 3 ------------ Motors 4
 *
 *                  Back
 */

public class SwerveDriveSubsystem extends SubsystemBase {
    @NotNull
    public final MotorController driveMotor;
    @NotNull
    public final MotorController turnMotor;


    @NotNull
    private final MotorSafety motorSafety = new MotorSafety() {
        @Override
        public void stopMotor() {
            stop();
        }

        @Override
        public String getDescription() {
            return "SwerveDrive";
        }
    };

    public SwerveDriveSubsystem(
            @NotNull MotorController drive,
            @NotNull MotorController turn
    ) {
        driveMotor = drive;
        turnMotor = turn;
    }

    public void initialize() {
        driveMotor.stopMotor();
    }

    public void stop() {
        driveMotor.stopMotor();
        motorSafety.feed();
    }

    public void swerve(double drive, double turn){
        driveMotor.set(drive);
        turnMotor.set(turn);
        motorSafety.feed();
    }
}
