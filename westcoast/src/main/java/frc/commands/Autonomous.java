package frc.commands;


import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.commands.drive.LeaveLine;
import frc.commands.magazine.Shoot;
import frc.commands.targeting.AlignToTarget;
import frc.commands.targeting.SetupShot;
import frc.robot.definition.Definition;
import frc.vision.LimelightRunner;
import frc.vision.VisionRunner;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

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

    @NotNull
    public static Command setupShot(@NotNull Definition definiton, double distance){
        return new SetupShot(
                definiton,
                distance
        );
    }

    public static Command shootAndLeave(@NotNull Definition definition, double speed) {
        return shoot(definition, () -> speed, () -> Shoot.Mode.Full)
                .andThen(move(definition, 1.5));
    }

    public static Command visionAuton(
            @NotNull Definition definition,
            @NotNull BooleanSupplier ballAcceptanceStrategy,
            double speed) {
        return deploy(definition)
                .andThen(
                        move(definition, 1.0)
                                .alongWith(
                                        collect(definition, ballAcceptanceStrategy)
                                )
                                .withTimeout(2.5)
                                .until(definition.subsystems.magazine::hasIndexSensorBreak)
                )
                .andThen(setupShot(definition, Units.inchesToMeters(55)))
                    .withTimeout(3)
                .andThen(shoot(definition, () -> speed, () -> Shoot.Mode.Full));
                //.andThen(move(definition, 1.5));
    }

    public static Command shootOutPickInShootOut(
            @NotNull Definition definition,
            @NotNull BooleanSupplier ballAcceptanceStrategy,
            double speed
    ) {
        return shoot(definition, () -> speed, () -> Shoot.Mode.Full)
                .alongWith(deploy(definition))
                .andThen(
                        move(definition, 1.0)
                                .alongWith(collect(definition, ballAcceptanceStrategy))
                                .until(definition.subsystems.magazine::hasFeederSensorBreak)
                )
                .andThen(move(definition, -1.0))
                .andThen(shoot(definition, () -> speed, () -> Shoot.Mode.Full))
                //shoot never stops so might have to fix that
                .andThen(move(definition, 1.5));
    }

    public static Command outPickInShootTwice(
            @NotNull Definition definition,
            @NotNull BooleanSupplier ballAcceptanceStrategy,
            double speed
    ) {
        return deploy(definition)
                .andThen(
                        move(definition, 1.0)
                                .alongWith(
                                        collect(definition, ballAcceptanceStrategy)
                                )
                                .withTimeout(2.5)
                                .until(definition.subsystems.magazine::hasIndexSensorBreak)
                )
                .andThen(move(definition, -1.0))
                .andThen(shoot(definition, () -> speed, () -> Shoot.Mode.Full))
                .andThen(move(definition, 1.5));
    }

    public static Command shootLeaveAndCollect(
            @NotNull Definition definition,
            @NotNull BooleanSupplier ballAcceptanceStrategy,
            double speed
    ) {
        return shoot(definition, () -> speed, () -> Shoot.Mode.Full)
                .alongWith(deploy(definition))
                .andThen(
                        move(definition, 1.5)
                                .alongWith(collect(definition, ballAcceptanceStrategy))
                                .until(definition.subsystems.magazine::hasFeederSensorBreak)
                );
    }
}