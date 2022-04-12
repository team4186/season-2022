package frc.commands.targeting

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj2.command.PIDCommand
import frc.subsystems.DriveTrainSubsystem

class StayOnTarget(
    controller: PIDController,
    private val drive: DriveTrainSubsystem
) : PIDCommand(
    /* controller = */ controller,
    /* measurementSource = */ { drive.vision.xOffset },
    /* setpoint = */ 0.0,
    /* useOutput = */ { drive.arcade(0.0, it.coerceIn(-0.4, 0.4), false) },
    /* ...requirements = */ drive
) {
    override fun end(interrupted: Boolean) {
        drive.stop()
    }
}