package frc.commands.drive

import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.definition.Ownership.DoubleParameter
import frc.subsystems.DriveTrainSubsystem

// THIS CLASS IS UNTUNED, PLEASE DO NOT USE IT UNTIL TUNED
class PerfectTurn(
    private val angle: Double,
    angleMultiplier: DoubleParameter<PerfectTurn>,
    left: ProfiledPIDController,
    right: ProfiledPIDController,
    drive: DriveTrainSubsystem
) : CommandBase() {
    private val angleMultiplier: Double
    private val left: ProfiledPIDController
    private val right: ProfiledPIDController
    private val drive: DriveTrainSubsystem
    private var wait = 0

    init {
        this.angleMultiplier = angleMultiplier.value
        this.left = left
        this.right = right
        this.drive = drive
    }

    override fun initialize() {
        wait = 0
        drive.rightEncoder.reset()
        drive.leftEncoder.reset()
        right.reset(0.0, 0.0)
        left.reset(0.0, 0.0)
    }

    override fun execute() {
        val target = angle * angleMultiplier
        drive.tank(
            left = left.calculate(-drive.leftEncoder.distance, target).coerceIn(-0.4, 0.4),
            right = right.calculate(-drive.rightEncoder.distance, -target).coerceIn(-0.4, 0.4),
            squareInputs = false
        )
        wait = if (right.atGoal() && left.atGoal()) wait + 1 else 0
    }

    override fun end(interrupted: Boolean) {
        drive.stop()
    }

    override fun isFinished(): Boolean {
        return wait >= 10
    }
}