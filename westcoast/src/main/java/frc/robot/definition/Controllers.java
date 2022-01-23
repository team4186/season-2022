package frc.robot.definition;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import org.jetbrains.annotations.NotNull;

public interface Controllers {
    @NotNull
    PIDController gyroDrive();

    @NotNull
    ProfiledPIDController leaveLine();

    @NotNull
    ProfiledPIDController perfectTurn();

    @NotNull
    PIDController alignToTarget();

    @NotNull
    PIDController stayOnTarget();
}
