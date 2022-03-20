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

    private final double[] debugInfo = new double[2];

    private double p = 0.0004;
    private double i = 0;
    private double d = 0.00075;
    private double iZone = 0.0;
    private double f = 0.0;
    private double maxOutput = 1;
    private double minOutput = 0;

    public ShooterSubsystem(@NotNull CANSparkMax shooterMotor) {
        this.shooterMotor = shooterMotor;
        pidController = shooterMotor.getPIDController();

        SmartDashboard.putNumber("Shooter P", p);
        SmartDashboard.putNumber("Shooter I", i);
        SmartDashboard.putNumber("Shooter I Zone", iZone);
        SmartDashboard.putNumber("Shooter D", d);
        SmartDashboard.putNumber("Shooter F", f);
        SmartDashboard.putNumber("Shooter Max Output", maxOutput);
        SmartDashboard.putNumber("Shooter Min Output", minOutput);
    }

    public double getSpeed() {
        return shooterMotor.getEncoder().getVelocity();
    }

    public void setSpeed(double speed) {

//        p = SmartDashboard.getNumber("Shooter P", 0);
//        i = SmartDashboard.getNumber("Shooter I", 0);
//        iZone = SmartDashboard.getNumber("Shooter I Zone", 0);
//        d = SmartDashboard.getNumber("Shooter D", 0);
//        f = SmartDashboard.getNumber("Shooter F", 0);
//        maxOutput = SmartDashboard.getNumber("Shooter Max Output", 0);
//        minOutput = SmartDashboard.getNumber("Shooter P", 0);

        pidController.setP(p);
        pidController.setI(i);
        pidController.setIZone(iZone);
        pidController.setD(d);
        pidController.setFF(f);
        pidController.setOutputRange(minOutput, maxOutput);

        pidController.setReference(speed, CANSparkMax.ControlType.kVelocity);

        debugInfo[0] = speed;
        debugInfo[1] = shooterMotor.getEncoder().getVelocity();

        SmartDashboard.putNumber("Shooter velocity", shooterMotor.getEncoder().getVelocity());
    }

    public void stop() {
        shooterMotor.stopMotor();
    }
}
