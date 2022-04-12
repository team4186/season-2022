package frc.commands.climb

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.subsystems.ClimberSubsystem

private const val deployGoal = 50.0 //Make this value line up to motors
private const val finalGoal = 130.0 //and this one (both are in terms of motor rotations)

class Climb(
    private val climber: ClimberSubsystem
) : CommandBase() {
    private enum class State {
        Deploying,
        Climbing,
        End
    }

    private var state = State.Deploying

    init {
        addRequirements(climber)
    }

    override fun initialize() {
        state = when {
            //tolerance for climber (5 revolutions is 1/2 a shaft rotation)
            climber.position < deployGoal - 8 -> State.Deploying
            climber.position < finalGoal -> State.Climbing
            else -> State.End
        }
    }

    override fun execute() {
        when (state) {
            State.End -> climber.stop()
            State.Deploying -> deploy()
            State.Climbing -> climb()
        }
    }

    private fun deploy() {
        when {
            climber.position >= deployGoal -> state = State.End
            else -> {
                climber.setClimberConfigDeploy()
                climber.position = deployGoal
            }
        }
    }

    private fun climb() {
        when {
            climber.position >= finalGoal -> state = State.End
            else -> {
                climber.setClimberConfigClimb()
                climber.position = finalGoal
            }
        }
    }

    override fun end(interrupted: Boolean) {
        climber.stop()
    }

    override fun isFinished(): Boolean {
        return state == State.End
    }
}