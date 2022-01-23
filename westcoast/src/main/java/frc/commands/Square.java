package frc.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.DriveCommands.leaveLine;
import static frc.commands.Commands.DriveCommands.perfectTurn;

public interface Square {
    @NotNull
    static Command square(@NotNull Definition definition, double size) {
        return new SequentialCommandGroup(
                leaveLine(definition, size),
                new WaitCommand(1.0),
                perfectTurn(definition, 90.0),
                new WaitCommand(1.0),

                leaveLine(definition, size),
                new WaitCommand(1.0),
                perfectTurn(definition, 90.0),
                new WaitCommand(1.0),

                leaveLine(definition, size),
                new WaitCommand(1.0),
                perfectTurn(definition, 90.0),
                new WaitCommand(1.0),

                leaveLine(definition, size),
                new WaitCommand(1.0),
                perfectTurn(definition, 90.0)
        );
    }
}