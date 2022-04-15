package frc.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.SparkMaxPIDController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase

class ShooterSubsystem(
    private val shooterMotor: CANSparkMax,
    configurator: SparkMaxPIDController.() -> Unit
) : SubsystemBase() {
    private val pidController: SparkMaxPIDController = shooterMotor.pidController.also(configurator)
    private val debugVelocityData = doubleArrayOf(0.0, 0.0)

    var speed: Double
        get() = shooterMotor.encoder.velocity
        set(speed) {
            debugVelocityData[1] = speed
            pidController.setReference(speed, CANSparkMax.ControlType.kVelocity)
        }

    fun stop() {
        debugVelocityData[1] = 0.0
        shooterMotor.stopMotor()
    }

    override fun periodic() {
        debugVelocityData[0] = shooterMotor.encoder.velocity
        SmartDashboard.putNumberArray("Shooter Velocity Graph", debugVelocityData)
    }
}