package frc.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;

public class ShooterSubsystem extends SubsystemBase {

    @NotNull
    private final MotorController shooterMotor;


    public ShooterSubsystem(@NotNull MotorController shooterMotor) {
        this.shooterMotor = shooterMotor;
    }

    public void setSpeed(double speed) {
        shooterMotor.set(speed);
    }

    public void stop() {
        shooterMotor.stopMotor();
    }
}
