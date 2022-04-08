package frc.commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.commands.drive.LeaveLine;
import frc.commands.intake.IntakeCollect;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.IntakeCommands.collect;
import static frc.commands.Commands.IntakeCommands.deploy;
import static frc.commands.Commands.ShooterCommands.shoot;

public final class Autonomous {
    @NotNull
    public static Command move(@NotNull Definition definition, double distance) {
        return new LeaveLine(
                distance,
                definition.controllers.leaveLine(),
                definition.controllers.leaveLine(),
                definition.subsystems.driveTrain
        );
    }

    public static Command shootAndLeave(@NotNull Definition definition) {
        return shoot(definition, () -> 3500.0)
                .andThen(move(definition, 1.5));
    }

    public static Command shootOutPickInShootOut(@NotNull Definition definition, IntakeCollect.ColorSupplier color) {
        return shoot(definition, () -> 3500.0)
                .alongWith(deploy(definition))
                .andThen(
                        move(definition, 1.0)
                                .alongWith(
                                        collect(definition, color)
                                )
                                .until(definition.subsystems.magazine::hasFeederSensorBreak)
                )
                .andThen(move(definition, -1.0))
                .andThen(shoot(definition, () -> 3500.0))
                .andThen(move(definition, 1.5));
    }

    public static Command outPickInShootTwice(@NotNull Definition definition, IntakeCollect.ColorSupplier color) {
        return deploy(definition)
                .andThen(
                        move(definition, 1.0)
                                .alongWith(
                                        collect(definition, color)
                                )
                                .withTimeout(2.5)
                                .until(definition.subsystems.magazine::hasIndexSensorBreak)
                )
                .andThen(move(definition, -1.0))
                .andThen(shoot(definition, () -> 3500.0))
                .andThen(move(definition, 1.5));
    }
}