package frc.subsystems;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.vision.VisionRunner;
import org.jetbrains.annotations.NotNull;

public class DriveTrainSubsystem extends SubsystemBase {

    @NotNull
    public final DifferentialDrive drive;
    @NotNull
    public final Encoder leftEncoder;
    @NotNull
    public final Encoder rightEncoder;
    @NotNull
    public final Gyro gyro;
    @NotNull
    public final VisionRunner vision;

    public DriveTrainSubsystem(
            @NotNull MotorController left,
            @NotNull MotorController right,
            @NotNull Encoder leftEncoder,
            @NotNull Encoder rightEncoder,
            @NotNull Gyro gyro,
            @NotNull VisionRunner vision
    ) {
        this.drive = new DifferentialDrive(left, right);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.gyro = gyro;
        this.vision = vision;
    }

    public void initialize() {
        drive.stopMotor();
        leftEncoder.reset();
        rightEncoder.reset();
        gyro.reset();

        drive.setSafetyEnabled(false);
    }

    public void stop() {
        drive.stopMotor();
    }

    public void arcade(double forward, double turn, boolean squareInputs) {
        drive.arcadeDrive(forward, turn, squareInputs);
    }

    public void tank(double left, double right, boolean squareInputs) {
        drive.tankDrive(left, right, squareInputs);
    }
}