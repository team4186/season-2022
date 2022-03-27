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
    public static Command move(double distance, @NotNull Definition definition) {
        return new LeaveLine(
                distance,
                definition.controllers.leaveLine(),
                definition.controllers.leaveLine(),
                definition.subsystems.driveTrain
        );
    }

    public static Command shootAndLeave(@NotNull Definition definition) {
        return shoot(definition, () -> 3100.0)
                .andThen(move(3.0, definition));
    }

    public static Command fullAuto(@NotNull Definition definition, IntakeCollect.ColorSupplier color) {
        return shoot(definition, () -> 3100.0)
                .alongWith(deploy(definition))
                .andThen(
                        move(3.0, definition)
                                .alongWith(
                                        collect(definition, color)
                                                .until(definition.subsystems.magazine::hasFeederSensorBreak)
                                )
                )
                .andThen(move(-3.0, definition))
                .andThen(shoot(definition, () -> 3100.0))
                .andThen(move(3.0, definition));
    }
}