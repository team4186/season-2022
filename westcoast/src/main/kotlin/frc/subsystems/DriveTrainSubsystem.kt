package frc.subsystems

import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.MotorSafety
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import edu.wpi.first.wpilibj.interfaces.Gyro
import edu.wpi.first.wpilibj.motorcontrol.MotorController
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.vision.VisionRunner

class DriveTrainSubsystem(
    private val leftMotor: MotorController,
    private val rightMotor: MotorController,
    val leftEncoder: Encoder,
    val rightEncoder: Encoder,
    val gyro: Gyro,
    val vision: VisionRunner
) : SubsystemBase() {
    private val drive: DifferentialDrive = DifferentialDrive(leftMotor, rightMotor)

    private val motorSafety: MotorSafety = object : MotorSafety() {
        override fun stopMotor() {
            stop()
        }

        override fun getDescription(): String {
            return "EncoderDrive"
        }
    }

    fun initialize() {
        drive.stopMotor()
        leftEncoder.reset()
        rightEncoder.reset()
        gyro.reset()
        drive.isSafetyEnabled = false
    }

    fun stop() {
        drive.stopMotor()
        motorSafety.feed()
    }

    fun arcade(forward: Double, turn: Double, squareInputs: Boolean) {
        drive.arcadeDrive(forward, turn, squareInputs)
    }

    fun tank(left: Double, right: Double, squareInputs: Boolean) {
        drive.tankDrive(left, right, squareInputs)
    }

    fun setMotorOutput(left: Double, right: Double) {
        leftMotor.set(left)
        rightMotor.set(right)
        motorSafety.feed()
    }
}