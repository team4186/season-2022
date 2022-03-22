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
    SparkMaxController shooterController();

    final class SparkMaxController {
        private final double p, i, d, iZone, ff, maxOutput, minOutput;

        public SparkMaxController(double p, double i, double d, double iZone, double ff, double maxOutput, double minOutput) {
            this.p = p;
            this.i = i;
            this.d = d;
            this.iZone = iZone;
            this.ff = ff;
            this.maxOutput = maxOutput;
            this.minOutput = minOutput;
        }

        public SparkMaxController(double p, double i, double d, double ff, double minOutput) {
            this(p, i, d, 0.0, ff, 0.0, minOutput);
        }

        public void configureController(SparkMaxPIDController controller) {
            controller.setP(p);
            controller.setI(i);
            controller.setIZone(iZone);
            controller.setD(d);
            controller.setFF(ff);
            controller.setOutputRange(minOutput, maxOutput);
        }
    }
}

