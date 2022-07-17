package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.variants.ShinDestroyer;
import frc.robot.variants.SwerveDestroyer;

public final class Main {
    private Main() {
    }

    public static void main(String... args) {
        RobotBase.startRobot(() -> new Robot(SwerveDestroyer.definition()));
    }
}
