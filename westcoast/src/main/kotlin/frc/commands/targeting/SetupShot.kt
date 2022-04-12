package frc.commands.targeting

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.subsystems.DriveTrainSubsystem

class SetupShot(
    private val turn: PIDController,
    private val forward: PIDController,
    private val drive: DriveTrainSubsystem,
    private val distance: () -> Double
) : CommandBase() {

    private var turnOnTarget = 0
    private var forwardOnTarget = 0
    private var targetLost = 0
    private val hasTarget = drive.vision.hasTarget

    override fun initialize() {
        turn.reset()
        forward.reset()
        turnOnTarget = 0
        forwardOnTarget = 0
        targetLost = 0
    }

    override fun execute() {
        drive.arcade(
            forward.calculate(drive.vision.distance, distance()).coerceIn(-0.1, 0.1),
            -turn.calculate(drive.vision.xOffset, 0.0).coerceIn(-0.2, 0.2),
            false
        )
        turnOnTarget = if (turn.atSetpoint()) turnOnTarget + 1 else 0
        forwardOnTarget = if (forward.atSetpoint()) forwardOnTarget + 1 else 0
        targetLost = if (drive.vision.hasTarget) targetLost + 1 else 0
    }

    override fun end(interrupted: Boolean) {
        drive.stop()
    }

    override fun isFinished(): Boolean {
        return turnOnTarget > 10 && forwardOnTarget > 10 || !hasTarget || targetLost > 5
    }
}