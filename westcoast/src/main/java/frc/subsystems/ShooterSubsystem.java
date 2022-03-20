package frc.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;

public class ShooterSubsystem extends SubsystemBase {
    public static double MAX_SPEED = 5350.0;
    @NotNull
    private final CANSparkMax shooterMotor;
    private final SparkMaxPIDController pidController;

    private final double p = 0.00015;
    private final double i = 0;
    private final double d = 0.0007;
    private final double iZone = 0.0;
    private final double f = 0.0002;
    private final double maxOutput = 1;
    private final double minOutput = 0;

    public ShooterSubsystem(@NotNull CANSparkMax shooterMotor) {
        this.shooterMotor = shooterMotor;
        pidController = shooterMotor.getPIDController();
    }

    public double getSpeed() {
        return shooterMotor.getEncoder().getVelocity();
    }

    public void setSpeed(double speed) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setIZone(iZone);
        pidController.setD(d);
        pidController.setFF(f);
        pidController.setOutputRange(minOutput, maxOutput);

        pidController.setReference(speed, CANSparkMax.ControlType.kVelocity);
    }

    public void stop() {
        shooterMotor.stopMotor();
    }
}
