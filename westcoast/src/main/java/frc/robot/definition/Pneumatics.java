package frc.robot.definition;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.jetbrains.annotations.NotNull;

public class Pneumatics {
    @NotNull
    public final DoubleSolenoid intakeDeployLeft;
    @NotNull
    public final DoubleSolenoid intakeDeployRight;

    public Pneumatics(
            @NotNull DoubleSolenoid intakeDeployLeft,
            @NotNull DoubleSolenoid intakeDeployRight
    ) {
        this.intakeDeployLeft = intakeDeployLeft;
        this.intakeDeployRight = intakeDeployRight;
    }
}
