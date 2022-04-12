package frc.commands.intake

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.subsystems.IntakeSubsystem
import frc.subsystems.MagazineSubsystem

class IntakeCollect(
    private val intake: IntakeSubsystem,
    private val magazine: MagazineSubsystem,
    private val ballAcceptanceStrategy: () -> Boolean,
    private val rejectTickCount: Int,
    private val reverseIntakeTickCount: Int,
    private val finishWhenFull: Boolean
) : CommandBase() {
    private enum class State {
        End,
        Full,
        Collecting,
        Rejecting,
        AcceptingToFeeder,
        AcceptingToIndex
    }

    init {
        addRequirements(intake, magazine)
    }

    private var state = State.Collecting
    override fun initialize() {
        state = State.Collecting
    }

    override fun execute() {
        when (state) {
            State.Full -> full()
            State.Collecting -> {
                reachReject = false
                reverseIntakeRunningTime = 0
                collect()
            }
            State.Rejecting -> reject()
            State.AcceptingToFeeder -> acceptToFeeder()
            State.AcceptingToIndex -> acceptToIndex()
            State.End -> Unit
        }
    }

    private fun full() {
        magazine.stopAll()
        intake.stop()
        state = when {
            finishWhenFull -> State.End
            !magazine.hasIndexSensorBreak() || !magazine.hasFeederSensorBreak() -> State.Collecting
            else -> state
        }
    }

    private fun collect() {
        if (!magazine.hasIndexSensorBreak()) {
            intake.start()
            magazine.startIndexMotor()
        } else {
            intake.stop()
            magazine.stopIndexMotor()
            state = when {
                !ballAcceptanceStrategy() -> State.Rejecting
                !magazine.hasFeederSensorBreak() -> State.AcceptingToFeeder
                else -> State.AcceptingToIndex
            }
        }
    }

    private var rejectRunningTime = 0
    private var reachReject = false
    private fun reject() {
        if (magazine.hasRejectSensorBreak()) {
            reachReject = true
            rejectRunningTime = 0
        }
        when {
            !reachReject -> {
                magazine.startIndexMotor()
                magazine.startRejectMotor()
            }
            rejectRunningTime < rejectTickCount -> {
                rejectRunningTime++
                magazine.startIndexMotor()
                magazine.startRejectMotor()
            }
            else -> {
                magazine.stopIndexMotor()
                magazine.stopRejectMotor()
                state = State.Collecting
            }
        }
    }

    private fun acceptToFeeder() {
        if (!magazine.hasFeederSensorBreak()) {
            magazine.startIndexMotor()
            magazine.startFeederMotor()
            magazine.reverseRejectMotor()
        } else {
            magazine.stopIndexMotor()
            magazine.stopFeederMotor()
            magazine.stopRejectMotor()
            state = State.Collecting
        }
    }

    private var reverseIntakeRunningTime = 0

    private fun acceptToIndex() {
        if (reverseIntakeRunningTime < reverseIntakeTickCount) {
            reverseIntakeRunningTime++
            intake.reverse()
        } else {
            intake.stop()
            state = State.Full
        }
    }

    override fun end(interrupted: Boolean) {
        intake.stop()
        magazine.stopAll()
    }

    override fun isFinished(): Boolean {
        return state == State.End
    }
}