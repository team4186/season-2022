package frc.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.SparkMaxPIDController
import edu.wpi.first.wpilibj2.command.SubsystemBase

class ClimberSubsystem(
    private val climberMotor: CANSparkMax,
    private val climberConfigDeploy: SparkMaxPIDController.() -> Unit,
    private val climberConfigClimb: SparkMaxPIDController.() -> Unit
) : SubsystemBase() {
    private val pidController: SparkMaxPIDController = climberMotor.pidController.also(climberConfigDeploy)
    private var isClimbing = false

    var position: Double
        get() = climberMotor.encoder.position
        set(position) {
            pidController.setReference(position, CANSparkMax.ControlType.kPosition)
        }

    fun resetEncoder() {
        climberMotor.encoder.position = 0.0
    }

    fun setClimberConfigDeploy() {
        if (isClimbing) {
            pidController.climberConfigDeploy()
            isClimbing = false
        }
    }

    fun setClimberConfigClimb() {
        if (!isClimbing) {
            pidController.climberConfigClimb()
            isClimbing = true
        }
    }

    fun stop() {
        climberMotor.stopMotor()
    }

    override fun periodic() {}
}