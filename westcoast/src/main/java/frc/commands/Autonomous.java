package frc.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.commands.drive.LeaveLine;
import frc.commands.magazine.Shoot;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

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
}