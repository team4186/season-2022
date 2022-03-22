package frc.commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.commands.drive.LeaveLine;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.ShooterCommands.shoot;

public final class Autonomous {
    @NotNull
    public static Command move(double encoderTicks, @NotNull Definition definition) {

        return new LeaveLine(
                encoderTicks,
                definition.controllers.leaveLine(),
                definition.controllers.leaveLine(),
                definition.subsystems.driveTrain
        );
    }

    public static Command shootAndLeave(@NotNull Definition definition) {
        return shoot(definition, ()-> 4000.0)
                .andThen(move(512.0, definition));
    }
}