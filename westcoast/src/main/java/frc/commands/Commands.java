package frc.commands;

import edu.wpi.first.wpilibj.util.Color;
import frc.commands.drive.*;
import frc.commands.intake.IntakeCollect;
import frc.commands.intake.IntakeDeploy;
import frc.commands.intake.IntakeRetrieve;
import frc.commands.magazine.Shoot;
import frc.commands.targeting.AlignToTarget;
import frc.commands.targeting.FindTarget;
import frc.commands.targeting.StayOnTarget;
import frc.robot.definition.Controllers;
import frc.robot.definition.Definition;
import frc.robot.definition.Input;
import frc.robot.definition.Parameters;
import org.jetbrains.annotations.NotNull;

public interface Commands {
    interface TeleopCommands {
        @NotNull
        static TeleopDrive raw(@NotNull Definition definition) {
            Input input = definition.input;
            return new TeleopDrive(
                    input.joystick,
                    input.attenuate,
                    input.invert,
                    definition.subsystems.driveTrain
            );
        }

        @NotNull
        static GyroDrive gyroAssisted(@NotNull Definition definition) {
            Input input = definition.input;
            return new GyroDrive(
                    definition.controllers.gyroDrive(),
                    input.joystick,
                    definition.subsystems.driveTrain
            );
        }

        @NotNull
        static EncoderDrive encodedAssisted(@NotNull Definition definition) {
            Input input = definition.input;
            return new EncoderDrive(
                    input.joystick,
                    input.invert,
                    input.turnInPlace,
                    definition.subsystems.driveTrain
            );
        }
    }

    interface DriveCommands {
        @NotNull
        static DistanceTravel leaveLine(@NotNull Definition definition, double distance) {
            final Controllers controllers = definition.controllers;
            final Parameters parameters = definition.parameters;
            return new DistanceTravel(
                    distance,
                    parameters.leaveLineDistanceMultiplier,
                    controllers.leaveLine(),
                    controllers.leaveLine(),
                    definition.subsystems.driveTrain
            );
        }

        @NotNull
        static PerfectTurn perfectTurn(@NotNull Definition definition, double angle) {
            final Controllers controllers = definition.controllers;
            final Parameters parameters = definition.parameters;
            return new PerfectTurn(
                    angle,
                    parameters.perfectTurnAngleMultiplier,
                    controllers.perfectTurn(),
                    controllers.perfectTurn(),
                    definition.subsystems.driveTrain
            );
        }

        @NotNull
        static AlignToTarget alignToTarget(@NotNull Definition definition) {
            return new AlignToTarget(
                    definition.controllers.alignToTarget(),
                    definition.subsystems.driveTrain
            );
        }

        @NotNull
        static StayOnTarget stayOnTarget(@NotNull Definition definition) {
            return new StayOnTarget(
                    definition.controllers.stayOnTarget(),
                    definition.subsystems.driveTrain
            );
        }

        @NotNull
        static FindTarget findTarget(@NotNull Definition definition) {
            return new FindTarget(
                    definition.subsystems.driveTrain
            );
        }
    }

    interface IntakeCommands {
        @NotNull
        static IntakeCollect collect(@NotNull Definition definition, @NotNull Color color) {
            return new IntakeCollect(
                    definition.subsystems.intake,
                    definition.subsystems.magazine,
                    color);
        }

        @NotNull
        static IntakeDeploy deploy(@NotNull Definition definition) {
            return new IntakeDeploy(definition.subsystems.intake);
        }

        @NotNull
        static IntakeRetrieve retrieve(@NotNull Definition definition) {
            return new IntakeRetrieve(definition.subsystems.intake);
        }
    }

    interface MagazineCommands {
    }

    interface ShooterCommands {
        @NotNull
        static Shoot shoot(@NotNull Definition definition) {
            return new Shoot(definition.subsystems.shooter, definition.subsystems.magazine);
        }
    }

}

