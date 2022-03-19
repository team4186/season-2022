package frc.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.commands.drive.AutonomousLeaveLine;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.DriveCommands.*;

public final class Autonomous {
    @NotNull
    public static Command move(double encoderTicks, @NotNull Definition definition) {

        return new AutonomousLeaveLine(
                encoderTicks,
                definition.controllers.leaveLine(),
                definition.controllers.leaveLine(),
                definition.subsystems.driveTrain
        );
    }


}