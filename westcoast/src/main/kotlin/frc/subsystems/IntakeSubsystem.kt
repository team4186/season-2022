package frc.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.motorcontrol.MotorController
import edu.wpi.first.wpilibj2.command.SubsystemBase

class IntakeSubsystem(
    private val deploy: DoubleSolenoid,
    private val intakeMotor: MotorController
) : SubsystemBase() {
    fun deploy() {
        deploy.set(DoubleSolenoid.Value.kReverse)
    }

    fun retrieve() {
        deploy.set(DoubleSolenoid.Value.kForward)
    }

    fun start() {
        intakeMotor.set(-0.8)
    }

    fun reverse() {
        intakeMotor.set(0.8)
    }

    fun stop() {
        intakeMotor.stopMotor()
    }
}