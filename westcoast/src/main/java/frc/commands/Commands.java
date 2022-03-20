package frc.commands;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.commands.drive.*;
import frc.commands.intake.IntakeCollect;
import frc.commands.magazine.Shoot;
import frc.commands.targeting.AlignToTarget;
import frc.commands.targeting.FindTarget;
import frc.commands.targeting.StayOnTarget;
import frc.robot.definition.Controllers;
import frc.robot.definition.Definition;
import frc.robot.definition.Input;
import frc.robot.definition.Parameters;
import frc.subsystems.IntakeSubsystem;
import frc.subsystems.MagazineSubsystem;
import frc.subsystems.ShooterSubsystem;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

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
                    definition.subsystems.driveTrain,
                    false
            );
        }
    }

    interface DriveCommands {
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
        static IntakeCollect collect(@NotNull Definition definition, @NotNull IntakeCollect.ColorSupplier color) {
            return new IntakeCollect(
                    definition.subsystems.intake,
                    definition.subsystems.magazine,
                    color,
                    50,
                    50,
                    true
            );
        }

        @NotNull
        static Command collect(@NotNull Definition definition) {
            IntakeSubsystem intake = definition.subsystems.intake;
            return new StartEndCommand(
                    intake::start,
                    intake::stop,
                    intake
            );
        }

        @NotNull
        static Command eject(@NotNull Definition definition) {
            IntakeSubsystem intake = definition.subsystems.intake;
            return new StartEndCommand(
                    intake::reverse,
                    intake::stop,
                    intake
            );
        }

        @NotNull
        static Command deploy(@NotNull Definition definition) {
            return new InstantCommand(
                    definition.subsystems.intake::deploy,
                    definition.subsystems.intake
            );
        }

        @NotNull
        static Command retrieve(@NotNull Definition definition) {
            return new InstantCommand(
                    definition.subsystems.intake::retrieve,
                    definition.subsystems.intake
            );
        }
    }

    interface MagazineCommands {
        @NotNull
        static Command collectToFeeder(@NotNull Definition definition) {
            MagazineSubsystem magazine = definition.subsystems.magazine;
            return new StartEndCommand(
                    () -> {
                        magazine.startFeederMotor();
                        magazine.reverseRejectMotor();
                        magazine.startIndexMotor();
                    },
                    () -> {
                        magazine.stopFeederMotor();
                        magazine.stopRejectMotor();
                        magazine.stopIndexMotor();
                    },
                    magazine
            ).until(magazine::hasFeederSensorBreak);
        }

        @NotNull
        static Command collectToIndex(@NotNull Definition definition) {
            MagazineSubsystem magazine = definition.subsystems.magazine;
            return new StartEndCommand(
                    magazine::startIndexMotor,
                    magazine::stopIndexMotor,
                    magazine
            ).until(magazine::hasIndexSensorBreak);
        }

        @NotNull
        static Command collectToReject(@NotNull Definition definition) {
            MagazineSubsystem magazine = definition.subsystems.magazine;
            return new StartEndCommand(
                    () -> {
                        magazine.startRejectMotor();
                        magazine.startIndexMotor();
                    },
                    () -> {
                        magazine.stopRejectMotor();
                        magazine.stopIndexMotor();
                    },
                    magazine
            ).until(magazine::hasRejectSensorBreak);
        }

        @NotNull
        static Command ejectFeeder(@NotNull Definition definition) {
            MagazineSubsystem magazine = definition.subsystems.magazine;
            return new StartEndCommand(
                    () -> {
                        magazine.reverseFeederMotor();
                        magazine.startRejectMotor();
                    },
                    () -> {
                        magazine.stopFeederMotor();
                        magazine.stopRejectMotor();
                    },
                    magazine
            );
        }

        @NotNull
        static Command ejectIndex(@NotNull Definition definition) {
            MagazineSubsystem magazine = definition.subsystems.magazine;
            return new StartEndCommand(
                    () -> {
                        magazine.startIndexMotor();
                        magazine.startRejectMotor();
                    },
                    () -> {
                        magazine.stopIndexMotor();
                        magazine.stopRejectMotor();
                    },
                    magazine
            );
        }

        @NotNull
        static Command ejectAll(@NotNull Definition definition) {
            return ejectFeeder(definition)
                    .withTimeout(1.0)
                    .andThen(ejectIndex(definition).withTimeout(1.0))
                    .alongWith(IntakeCommands.eject(definition).withTimeout(1.0));
        }
    }

    interface ShooterCommands {
        @NotNull
        static Shoot shoot(@NotNull Definition definition, @NotNull DoubleSupplier velocity) {
            return new Shoot(
                    definition.subsystems.shooter,
                    definition.subsystems.magazine,
                    velocity,
                    50,
                    25
            );
        }

    }

    interface TestCommands {
        @NotNull
        static Command runMotor(@NotNull MotorController controller, @NotNull DoubleSupplier velocity) {
            return new StartEndCommand(
                    () -> controller.set(velocity.getAsDouble()),
                    controller::stopMotor
            );
        }

        @NotNull
        static Command shooterFlow(@NotNull Definition definition, @NotNull DoubleSupplier velocity) {
            IntakeSubsystem intake = definition.subsystems.intake;
            ShooterSubsystem shooter = definition.subsystems.shooter;
            MagazineSubsystem magazine = definition.subsystems.magazine;
            return new StartEndCommand(
                    () -> {
                        shooter.setSpeed(velocity.getAsDouble());
                        intake.start();
                        magazine.startIndexMotor();
                        magazine.startFeederMotor();
                        magazine.reverseRejectMotor();
                    },
                    () -> {
                        shooter.stop();
                        intake.stop();
                        magazine.stopIndexMotor();
                        magazine.stopFeederMotor();
                        magazine.stopRejectMotor();
                    },
                    intake, magazine, shooter
            );
        }

        @NotNull
        static Command rejectFlow(@NotNull Definition definition) {
            IntakeSubsystem intake = definition.subsystems.intake;
            MagazineSubsystem magazine = definition.subsystems.magazine;
            return new StartEndCommand(
                    () -> {
                        intake.start();
                        magazine.startIndexMotor();
                        magazine.startRejectMotor();
                    },
                    () -> {
                        intake.stop();
                        magazine.stopIndexMotor();
                        magazine.stopRejectMotor();
                    },
                    intake, magazine
            );
        }
    }
}

