package frc.subsystems;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.vision.VisionRunner;
import org.jetbrains.annotations.NotNull;

public class DriveTrainSubsystem extends SubsystemBase {

    @NotNull
    public final MotorController leftMotor;
    @NotNull
    public final MotorController rightMotor;

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


    @NotNull
    private final MotorSafety motorSafety = new MotorSafety() {
        @Override
        public void stopMotor() {
            stop();
        }

        @Override
        public String getDescription() {
            return "EncoderDrive";
        }
    };

    public DriveTrainSubsystem(
            @NotNull MotorController left,
            @NotNull MotorController right,
            @NotNull Encoder leftEncoder,
            @NotNull Encoder rightEncoder,
            @NotNull Gyro gyro,
            @NotNull VisionRunner vision
    ) {
        leftMotor = left;
        rightMotor = right;
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
        motorSafety.feed();
    }

    public void arcade(double forward, double turn, boolean squareInputs) {
        drive.arcadeDrive(forward, turn, squareInputs);
    }

    public void tank(double left, double right, boolean squareInputs) {
        drive.tankDrive(left, right, squareInputs);
    }

    public void setMotorOutput(double left, double right) {
        leftMotor.set(left);
        rightMotor.set(right);
        motorSafety.feed();
    }
}