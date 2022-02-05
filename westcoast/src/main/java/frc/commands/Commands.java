package frc.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.commands.drive.*;
import frc.commands.magazine.*;
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

    interface MagazineCommands {
        @NotNull
        static IndexLogic index(@NotNull Definition definition) {
            return new IndexLogic(definition.subsystems.magazine);
        }

        @NotNull
        static IntakeLogic intake(@NotNull Definition definition) {
            return new IntakeLogic(definition.subsystems.magazine);
        }

        @NotNull
        static IntakeOut intakeOut(@NotNull Definition definition) {
            return new IntakeOut(definition.subsystems.magazine);
        }

        @NotNull
        static EverythingOut everythingOut(@NotNull Definition definition) {
            return new EverythingOut(definition.subsystems.magazine);
        }
    }

    interface ShooterCommands {
        @NotNull
        static Shoot shoot(@NotNull Definition definition) {
            return new Shoot(definition.subsystems.magazine);
        }

        @NotNull
        static ShooterAccelerator shooterAccelerator(@NotNull Definition definition) {
            return new ShooterAccelerator(definition.subsystems.magazine);
        }
    }

    interface Compound {
        @NotNull
        static Command shoot(@NotNull Definition definition) {
            return new SequentialCommandGroup(
                    DriveCommands.alignToTarget(definition),
                    SequentialCommandGroup.race(
                            ShooterCommands.shooterAccelerator(definition),
                            new WaitCommand(1.5)
                    ),
                    SequentialCommandGroup.race(
                            ShooterCommands.shoot(definition),
                            DriveCommands.stayOnTarget(definition),
                            new WaitCommand(4.0)
                    )
            );
        }

        @NotNull
        static Command intakeAndIndex(@NotNull Definition definition) {
            return new SequentialCommandGroup(
                    MagazineCommands.intake(definition),
                    new WaitCommand(0.2),
                    MagazineCommands.index(definition)
            );
        }
    }
}

