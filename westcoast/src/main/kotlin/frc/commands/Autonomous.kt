package frc.commands

import edu.wpi.first.wpilibj2.command.Command
import frc.commands.Commands.IntakeCommands.collect
import frc.commands.Commands.IntakeCommands.deploy
import frc.commands.Commands.ShooterCommands.shoot
import frc.commands.drive.LeaveLine
import frc.commands.magazine.Shoot
import frc.robot.definition.Definition

object Autonomous {
    fun Definition.move(distance: Double): Command {
        return LeaveLine(
            distance,
            controllers.leaveLine(),
            controllers.leaveLine(),
            subsystems.driveTrain
        )
    }

    fun Definition.shootAndLeave(speed: Double): Command {
        return shoot(
            velocity = { speed },
            mode = { Shoot.Mode.Full }
        ).andThen(move(1.5))
    }

    fun Definition.shootOutPickInShootOut(
        ballAcceptanceStrategy: () -> Boolean,
        speed: Double
    ): Command {
        return shoot(
            velocity = { speed },
            mode = { Shoot.Mode.Full }
        ).alongWith(deploy())
            .andThen(
                move(1.0)
                    .alongWith(collect(ballAcceptanceStrategy))
                    .until { subsystems.magazine.hasFeederSensorBreak() }
            )
            .andThen(move(-1.0))
            .andThen(shoot({ speed }, { Shoot.Mode.Full }))
            .andThen(move(1.5))
    }

    fun Definition.outPickInShootTwice(
        ballAcceptanceStrategy: () -> Boolean,
        speed: Double
    ): Command {
        return deploy()
            .andThen(
                move(1.0)
                    .alongWith(collect(ballAcceptanceStrategy))
                    .withTimeout(2.5)
                    .until { subsystems.magazine.hasIndexSensorBreak() }
            )
            .andThen(move(-1.0))
            .andThen(shoot({ speed }, { Shoot.Mode.Full }))
            .andThen(move(1.5))
    }

    fun Definition.shootLeaveAndCollect(
        ballAcceptanceStrategy: () -> Boolean,
        speed: Double
    ): Command {
        return shoot({ speed }, { Shoot.Mode.Full })
            .alongWith(deploy())
            .andThen(
                move(1.5)
                    .alongWith(collect(ballAcceptanceStrategy))
                    .until { subsystems.magazine.hasFeederSensorBreak() }
            )
    }
}