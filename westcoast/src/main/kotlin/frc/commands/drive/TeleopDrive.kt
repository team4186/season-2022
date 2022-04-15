package frc.commands.drive

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.subsystems.DriveTrainSubsystem
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.withSign

class TeleopDrive(
    private val inputYaw: () -> Double,
    private val inputThrottle: () -> Double,
    private val shouldAttenuate: () -> Boolean,
    private val forward: () -> Double,
    private val drive: DriveTrainSubsystem
) : CommandBase() {
    init {
        addRequirements(drive)
    }

    override fun execute() {
        val throttle: Double
        val yaw: Double
        if (shouldAttenuate()) {
            throttle = attenuated(forward() * inputThrottle())
            yaw = attenuated(-inputYaw())
        } else {
            throttle = full(forward() * inputThrottle())
            yaw = full(-inputYaw()).coerceIn(-0.6, 0.6)
        }
        drive.arcade(
            forward = -throttle,
            turn = -yaw,
            squareInputs = false
        )
    }

    override fun end(interrupted: Boolean) {
        drive.stop()
    }

    private fun full(value: Double): Double {
        return value
            .absoluteValue
            .pow(1.6)
            .withSign(value)
    }

    private fun attenuated(value: Double): Double {
        return 0.5 * value
    }
}