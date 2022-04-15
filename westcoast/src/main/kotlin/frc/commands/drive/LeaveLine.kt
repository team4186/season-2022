package frc.commands.drive

import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.subsystems.DriveTrainSubsystem

class LeaveLine(
    private val distance: Double,
    private val left: ProfiledPIDController,
    private val right: ProfiledPIDController,
    private val drive: DriveTrainSubsystem
) : CommandBase() {

    private var wait = 0

    override fun initialize() {
        wait = 0
        drive.rightEncoder.reset()
        drive.leftEncoder.reset()
        right.reset(0.0)
        left.reset(0.0)
    }

    override fun execute() {
        val rightOut = right.calculate(drive.rightEncoder.distance, distance).coerceIn(-0.4, 0.4)
        val leftOut = left.calculate(drive.leftEncoder.distance, distance).coerceIn(-0.4, 0.4)
        drive.setMotorOutput(leftOut, rightOut)
        wait = if (right.atGoal() && left.atGoal()) wait + 1 else 0
    }

    override fun end(interrupted: Boolean) {
        drive.stop()
    }

    override fun isFinished(): Boolean {
        return wait >= 10
    }
}