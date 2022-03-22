package frc.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.definition.Controllers.ControllerConfigurator;
import org.jetbrains.annotations.NotNull;

public class ShooterSubsystem extends SubsystemBase {
    public static double MAX_SPEED = 5350.0;
    @NotNull
    private final CANSparkMax shooterMotor;
    private final SparkMaxPIDController pidController;

    public ShooterSubsystem(@NotNull CANSparkMax shooterMotor, @NotNull ControllerConfigurator configurator) {
        this.shooterMotor = shooterMotor;
        pidController = shooterMotor.getPIDController();
        configurator.configure(pidController);
    }

    public double getSpeed() {
        return shooterMotor.getEncoder().getVelocity();
    }

    public void setSpeed(double speed) {
        pidController.setReference(speed, CANSparkMax.ControlType.kVelocity);
    }

    public void stop() {
        shooterMotor.stopMotor();
    }
}
