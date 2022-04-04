package frc.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.definition.Controllers.ControllerConfigurator;
import org.jetbrains.annotations.NotNull;

public class ShooterSubsystem extends SubsystemBase {
    @NotNull
    private final CANSparkMax shooterMotor;
    private final SparkMaxPIDController pidController;

    private final double[] debugVelocityData = {0.0, 0.0};

    public ShooterSubsystem(@NotNull CANSparkMax shooterMotor, @NotNull ControllerConfigurator configurator) {
        this.shooterMotor = shooterMotor;
        pidController = shooterMotor.getPIDController();
        configurator.configure(pidController);
    }

    public double getSpeed() {
        return shooterMotor.getEncoder().getVelocity();
    }

    public void setSpeed(double speed) {
        debugVelocityData[1] = speed;
        pidController.setReference(speed, CANSparkMax.ControlType.kVelocity);
    }

    public void stop() {
        debugVelocityData[1] = 0.0;
        shooterMotor.stopMotor();
    }

    @Override
    public void periodic() {
        debugVelocityData[0] = shooterMotor.getEncoder().getVelocity();
        SmartDashboard.putNumberArray("Shooter Velocity Graph", debugVelocityData);
    }
}
