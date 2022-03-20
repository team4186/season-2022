package frc.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;

public class ShooterSubsystem extends SubsystemBase {
    public static double MAX_SPEED = 5350.0;
    @NotNull
    private final CANSparkMax shooterMotor;

    public ShooterSubsystem(@NotNull CANSparkMax shooterMotor) {
        this.shooterMotor = shooterMotor;
    }

    public double getSpeed() {
        return shooterMotor.getEncoder().getVelocity();
    }

    public void setSpeed(double speed) {
        SmartDashboard.putNumber("Shooter Speed (target)", speed);
        // TODO 5676 is the spec for NEO motors we should use the embedded PID Controller to ensure
        // the speed control
        // should be controlled with a pid feedforwards
        // not with just.set because it's not feeding forwards
        shooterMotor.set(speed / MAX_SPEED);
    }

    public void stop() {
        shooterMotor.stopMotor();
    }
}
