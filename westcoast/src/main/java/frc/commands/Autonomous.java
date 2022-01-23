package frc.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.DriveCommands.*;

public final class Autonomous {
    @NotNull
    public static Command flex(@NotNull Definition definition) {

        return new SequentialCommandGroup(
                leaveLine(definition, -1.0),
                perfectTurn(definition, 180.0),
                leaveLine(definition, 9.0),
                perfectTurn(definition, 180.0),
                alignToTarget(definition)
        );
    }

    @NotNull
    public static Command center(@NotNull Definition definition, double distance, double angle) {
        return new SequentialCommandGroup(
                leaveLine(definition, distance),
                new WaitCommand(1.0),
                perfectTurn(definition, angle),
                new WaitCommand(1.0),
                alignToTarget(definition)
        );
    }

    @NotNull
    public static Command loadingBay(@NotNull Definition definition, double distance, double angle) {
        return new SequentialCommandGroup(
                leaveLine(definition, distance),
                new WaitCommand(1.0),
                perfectTurn(definition, angle),
                new WaitCommand(1.0),
                alignToTarget(definition)
        );
    }

    @NotNull
    public static Command target(@NotNull Definition definition, double distance) {
        return new SequentialCommandGroup(
                leaveLine(definition, distance),
                new WaitCommand(1.0),
                alignToTarget(definition)
        );
    }
}