package frc.commands.targeting

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj2.command.PIDCommand
import frc.subsystems.DriveTrainSubsystem

class AlignToTarget(
    controller: PIDController,
    private val drive: DriveTrainSubsystem
) : PIDCommand(
    controller,
    { drive.vision.xOffset },
    0.0,
    {
        drive.arcade(
            forward = 0.0,
            turn = if (drive.vision.hasTarget) it.coerceIn(-0.4,  0.4) else 0.0,
            squareInputs = false
        )
    }
) {

    private var wait = 0

    override fun initialize() {
        wait = 0
    }

    override fun execute() {
        wait = if (controller.atSetpoint()) wait + 1 else 0
    }

    override fun end(interrupted: Boolean) {
        drive.stop()
    }

    override fun isFinished(): Boolean {
        return wait > 10 && drive.vision.hasTarget
    }
}