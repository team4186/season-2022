package frc.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;

public class ShooterSubsystem extends SubsystemBase {

    @NotNull
    private final CANSparkMax shooterMotor;

    public ShooterSubsystem(@NotNull CANSparkMax shooterMotor) {
        this.shooterMotor = shooterMotor;
    }

    public double getSpeed() {
        return shooterMotor.getEncoder().getVelocity();
    }

    public void setSpeed(double speed) {
        // TODO 5676 is the spec for NEO motors we should use the embedded PID Controller to ensure
        // the speed control
        shooterMotor.set(speed / 5676.0);
    }

    public void stop() {
        shooterMotor.stopMotor();
    }
}
