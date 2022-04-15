package frc.robot.definition

import com.revrobotics.SparkMaxPIDController
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController

class Controllers(
    val leaveLine: () -> ProfiledPIDController,
    val setupShotTurn: () -> PIDController,
    val setupShotForward: () -> PIDController,
    val shooterConfig: SparkMaxPIDController.() -> Unit,
    val climber: Climber
) {
    data class Climber(
        val deploy: SparkMaxPIDController.() -> Unit,
        val climb: SparkMaxPIDController.() -> Unit
    )
}