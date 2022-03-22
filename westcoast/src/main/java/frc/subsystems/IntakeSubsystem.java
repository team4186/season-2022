package frc.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse;

public class IntakeSubsystem extends SubsystemBase {
    @NotNull
    private final DoubleSolenoid deploy;
    @NotNull
    private final MotorController intakeMotor;

    public IntakeSubsystem(
            @NotNull DoubleSolenoid deploy,
            @NotNull MotorController intakeMotor
    ) {
        this.deploy = deploy;
        this.intakeMotor = intakeMotor;
    }

    public void deploy() {
        deploy.set(kReverse);
    }

    public void retrieve() {
        deploy.set(kForward);
    }

    public void start() {
        intakeMotor.set(-0.8);
    }

    public void reverse() {
        intakeMotor.set(0.8);
    }

    public void stop() {
        intakeMotor.stopMotor();
    }
}
