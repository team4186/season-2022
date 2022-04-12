package frc.robot

import edu.wpi.first.wpilibj.RobotBase
import frc.robot.variants.ShinDestroyer

fun main() {
    RobotBase.startRobot { Robot(ShinDestroyer.definition()) }
}
