package frc.robot.definition;

import com.revrobotics.SparkMaxPIDController;
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
    
    @NotNull
    PIDController forwardAlignToTarget();

    void shooterConfig(@NotNull SparkMaxPIDController controller);

    void climberConfigDeploy(@NotNull SparkMaxPIDController controller);

    void climberConfigClimb(@NotNull SparkMaxPIDController controller);

    interface ControllerConfigurator {
        void configure(@NotNull SparkMaxPIDController controller);
    }
}
