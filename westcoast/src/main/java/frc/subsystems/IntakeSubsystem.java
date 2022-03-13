package frc.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse;

public class IntakeSubsystem extends SubsystemBase {
    @NotNull
    private final DoubleSolenoid left;
    @NotNull
    private final DoubleSolenoid right;
    @NotNull
    private final MotorController intakeMotor;

    public IntakeSubsystem(
            @NotNull DoubleSolenoid left,
            @NotNull DoubleSolenoid right,
            @NotNull MotorController intakeMotor
    ) {
        this.left = left;
        this.right = right;
        this.intakeMotor = intakeMotor;
    }

    public void deploy() {
        left.set(kForward);
        right.set(kForward);
    }

    public void retrieve() {
        left.set(kReverse);
        right.set(kReverse);
    }

    public void start() {
        intakeMotor.set(0.5);
    }

    public void stop() {
        intakeMotor.stopMotor();
    }
}
