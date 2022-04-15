package frc.commands.drive

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.button.Button
import frc.subsystems.DriveTrainSubsystem
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.withSign

private const val maxSpeed = 7.0 * 256.0 // encoders report ~1550-1600 pulses
private const val deadzone = 0.02
private const val deadzoneComplement = 1.0 / (1.0 - deadzone)

class EncoderDrive(
    private val inputYaw: () -> Double,
    private val inputThrottle: () -> Double,
    private val forward: () -> Double,
    private val turnInPlace: Button,
    private val drive: DriveTrainSubsystem,
    private val sendDebugData: Boolean
) : CommandBase() {
    private val left: PIDController
    private val right: PIDController
    private val leftData = doubleArrayOf(0.0, 0.0)
    private val rightData = doubleArrayOf(0.0, 0.0)

    init {
        val p = 0.0018
        val i = 0.0
        val d = 0.0
        left = PIDController(p, i, d).apply {
            setTolerance(1.0, 1.0)
            disableContinuousInput()
        }

        right = PIDController(p, i, d).apply {
            setTolerance(1.0, 1.0)
            disableContinuousInput()
        }

        addRequirements(drive)
    }

    override fun initialize() {
        drive.leftEncoder.reset()
        drive.rightEncoder.reset()
        right.reset()
        left.reset()
    }

    override fun execute() {
        // region Input
        val rawY = forward() * -inputThrottle().coerceIn(-1.0, 1.0)
        val rawX = inputYaw().coerceIn(-1.0, 1.0)
        var throttle = ((rawY.absoluteValue - deadzone) * deadzoneComplement).coerceAtLeast(0.0).withSign(rawY)
        var yaw = ((rawX.absoluteValue - deadzone) * deadzoneComplement).coerceAtLeast(0.0).withSign(rawX)

        // endregion


        // region Input -> Output
        var leftSpeed: Double
        var rightSpeed: Double
        if (!turnInPlace.get()) {
            throttle = (throttle * throttle).withSign(rawY)
            yaw = (yaw * yaw).withSign(rawX)
            leftSpeed = throttle + yaw
            rightSpeed = throttle - yaw
        } else {
            leftSpeed = throttle + throttle * yaw
            rightSpeed = throttle - throttle * yaw
        }

        // Normalize wheel speeds
        val maxMagnitude = max(leftSpeed.absoluteValue, rightSpeed.absoluteValue)
        if (maxMagnitude > 1.0) {
            leftSpeed /= maxMagnitude
            rightSpeed /= maxMagnitude
        }

        // endregion


        // region Feed the PIDs
        val leftSetpoint = leftSpeed * maxSpeed
        val rightSetpoint = rightSpeed * maxSpeed
        val leftOut = left.calculate(drive.leftEncoder.rate, leftSetpoint)
        val rightOut = right.calculate(drive.rightEncoder.rate, rightSetpoint)

        //endregion


        // region Output
        drive.setMotorOutput(leftOut, rightOut)

        // endregion


        // region Debug
        if (sendDebugData) {
            leftData[0] = drive.leftEncoder.rate
            leftData[1] = leftSetpoint
            rightData[0] = drive.rightEncoder.rate
            rightData[1] = rightSetpoint
            SmartDashboard.putBoolean("Revert", forward() > 0.0)
            SmartDashboard.putNumber("Forward", forward() * throttle)
            SmartDashboard.putNumber("Turn", yaw)
            SmartDashboard.putNumberArray("lE Rate", leftData)
            SmartDashboard.putNumber("lOutput", leftOut)
            SmartDashboard.putNumberArray("rE Rate", rightData)
            SmartDashboard.putNumber("rOutput", rightOut)
        }
        // endregion
    }

    override fun end(interrupted: Boolean) {
        drive.stop()
    }
}