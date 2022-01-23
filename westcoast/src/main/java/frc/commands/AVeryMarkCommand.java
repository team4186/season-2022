package frc.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Square.square;

public interface AVeryMarkCommand {
    @NotNull
    static Command create(@NotNull Definition definition) {
        return new SequentialCommandGroup(
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0),
                square(definition, 10.0)
        );
    }
}