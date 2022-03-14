package frc.robot.definition;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.jetbrains.annotations.NotNull;

public class Pneumatics {
    @NotNull
    public final DoubleSolenoid intakeDeploy;

    public Pneumatics(
            @NotNull DoubleSolenoid intakeDeploy
    ) {
        this.intakeDeploy = intakeDeploy;
    }
}
