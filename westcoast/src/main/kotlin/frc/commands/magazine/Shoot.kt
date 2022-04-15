package frc.commands.magazine

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.subsystems.MagazineSubsystem
import frc.subsystems.ShooterSubsystem

private const val SPEED_TOLERANCE = 10

class Shoot(
    private val shooter: ShooterSubsystem,
    private val magazine: MagazineSubsystem,
    private val targetVelocity: () -> Double,
    private val mode: () -> Mode,
    private val maxReloadTicks: Int,
    private val maxShooterDelay: Int
) : CommandBase() {
    private enum class State {
        End,
        Reloading,
        Accelerating,
        Shooting
    }

    enum class Mode {
        Single,
        Full
    }


    init {
        addRequirements(shooter, magazine)
    }

    private var state = State.Reloading
    override fun initialize() {
        state = State.Reloading
        reloadTimeout = maxReloadTicks
    }

    override fun execute() {
        when (state) {
            State.End -> Unit
            State.Reloading -> {
                shooterDelay = 0
                reloading()
            }
            State.Accelerating -> {
                shooterDelay = 0
                accelerating()
            }
            State.Shooting -> shooting()
        }
    }

    private var reloadTimeout = 0
    private fun reloading() {
        if (magazine.hasFeederSensorBreak()) {
            magazine.stopIndexMotor()
            magazine.stopRejectMotor()
            magazine.stopFeederMotor()
            state = State.Accelerating
            reloadTimeout = 0
        } else if (magazine.hasIndexSensorBreak()) {
            magazine.startFeederMotor()
            magazine.startIndexMotor()
            magazine.reverseRejectMotor()
            reloadTimeout = 0
        } else if (reloadTimeout++ >= maxReloadTicks) {
            magazine.stopIndexMotor()
            magazine.stopRejectMotor()
            magazine.stopFeederMotor()
            state = State.End
        }
    }

    private fun accelerating() {
        state = when {
            !magazine.hasFeederSensorBreak() -> State.Reloading
            isSpeedWithinTolerance -> State.Shooting
            else -> state
        }
    }

    private val isSpeedWithinTolerance: Boolean
        get() = shooter.speed in targetVelocity().let { speed -> speed - SPEED_TOLERANCE..speed + SPEED_TOLERANCE }

    private var shooterDelay = 0

    private fun shooting() {
        if (!magazine.hasFeederSensorBreak()) {
            state = when (mode()) {
                Mode.Single -> {
                    magazine.stopFeederMotor()
                    State.End
                }
                Mode.Full -> State.Reloading
            }
        } else if (!isSpeedWithinTolerance) {
            state = State.Accelerating
        } else if (mode() == Mode.Single || shooterDelay >= maxShooterDelay) {
            if (mode() == Mode.Full) {
                magazine.startIndexMotor()
                magazine.reverseRejectMotor()
            }
            magazine.startFeederMotor()
        } else {
            shooterDelay++
        }
    }

    override fun end(interrupted: Boolean) {
        //shooter.stop();
        magazine.stopIndexMotor()
        magazine.stopFeederMotor()
        magazine.stopRejectMotor()
    }

    override fun isFinished(): Boolean {
        return state == State.End
    }
}