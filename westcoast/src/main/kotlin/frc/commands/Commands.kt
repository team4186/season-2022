package frc.commands

import edu.wpi.first.wpilibj.motorcontrol.MotorController
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.StartEndCommand
import frc.commands.Commands.IntakeCommands.eject
import frc.commands.climb.Climb
import frc.commands.drive.CheesyDrive
import frc.commands.drive.EncoderDrive
import frc.commands.drive.TeleopDrive
import frc.commands.intake.IntakeCollect
import frc.commands.magazine.Shoot
import frc.commands.targeting.SetupShot
import frc.robot.definition.Definition

object Commands {
    object DriveCommands {
        fun Definition.raw(): TeleopDrive {
            var forward = 1.0
            input.invert.whenPressed(Runnable { forward *= -1.0 })

            return TeleopDrive(
                inputThrottle = { input.joystick.y },
                inputYaw = { input.joystick.x },
                shouldAttenuate = { input.attenuate.get() },
                forward = { forward },
                drive = subsystems.driveTrain
            )
        }

        fun Definition.cheesy(): Command {
            var forward = 1.0
            input.invert.whenPressed(Runnable { forward *= -1.0 })
            return CheesyDrive(
                inputThrottle = { input.joystick.y },
                inputYaw = { input.joystick.x },
                isHighGear = { !input.attenuate.get() },
                isQuickTurn = { !input.turnInPlace.get() },
                forward = { forward },
                drive = subsystems.driveTrain,
                sensitivityHigh = 0.5,
                sensitivityLow = 0.5
            )
        }

        fun Definition.encoderAssisted(): EncoderDrive {
            var forward = 1.0
            input.invert.whenPressed(Runnable { forward *= -1.0 })
            return EncoderDrive(
                inputThrottle = { input.joystick.y },
                inputYaw = { input.joystick.x },
                forward = { forward },
                turnInPlace = input.turnInPlace,
                drive = subsystems.driveTrain,
                sendDebugData = false
            )
        }
    }

    object AutonomousDriveCommands {
        fun Definition.setupShot(distance: () -> Double): Command {
            return SetupShot(
                turn = controllers.setupShotTurn(),
                forward = controllers.setupShotForward(),
                drive = subsystems.driveTrain,
                distance = distance
            )
        }
    }

    object IntakeCommands {
        fun Definition.collect(ballAcceptanceStrategy: () -> Boolean): IntakeCollect {
            return IntakeCollect(
                intake = subsystems.intake,
                magazine = subsystems.magazine,
                ballAcceptanceStrategy = ballAcceptanceStrategy,
                rejectTickCount = 50,
                reverseIntakeTickCount = 50,
                finishWhenFull = true
            )
        }

        fun Definition.eject(): Command {
            val intake = subsystems.intake
            return StartEndCommand(
                /* onInit = */ { intake.reverse() },
                /* onEnd = */ { intake.stop() },
                /* ...requirements = */ intake
            )
        }

        fun Definition.deploy(): Command {
            return InstantCommand(
                /* toRun = */ { subsystems.intake.deploy() },
                /* ...requirements = */ subsystems.intake
            )
        }

        fun Definition.retrieve(): Command {
            return InstantCommand(
                /* toRun = */ { subsystems.intake.retrieve() },
                /* ...requirements = */ subsystems.intake
            )
        }
    }

    object MagazineCommands {
        fun Definition.collectToFeeder(): Command {
            val magazine = subsystems.magazine
            return StartEndCommand(
                {
                    magazine.startFeederMotor()
                    magazine.reverseRejectMotor()
                    magazine.startIndexMotor()
                },
                {
                    magazine.stopFeederMotor()
                    magazine.stopRejectMotor()
                    magazine.stopIndexMotor()
                },
                magazine
            ).until { magazine.hasFeederSensorBreak() }
        }

        fun Definition.collectToIndex(): Command {
            val magazine = subsystems.magazine
            return StartEndCommand(
                { magazine.startIndexMotor() },
                { magazine.stopIndexMotor() },
                magazine
            ).until { magazine.hasIndexSensorBreak() }
        }

        fun Definition.collectToReject(): Command {
            val magazine = subsystems.magazine
            return StartEndCommand(
                {
                    magazine.startRejectMotor()
                    magazine.startIndexMotor()
                },
                {
                    magazine.stopRejectMotor()
                    magazine.stopIndexMotor()
                },
                magazine
            ).until { magazine.hasRejectSensorBreak() }
        }

        fun Definition.ejectFeeder(): Command {
            val magazine = subsystems.magazine
            return StartEndCommand(
                {
                    magazine.reverseFeederMotor()
                    magazine.startRejectMotor()
                },
                {
                    magazine.stopFeederMotor()
                    magazine.stopRejectMotor()
                },
                magazine
            )
        }

        fun Definition.ejectIndex(): Command {
            val magazine = subsystems.magazine
            return StartEndCommand(
                {
                    magazine.startIndexMotor()
                    magazine.startRejectMotor()
                },
                {
                    magazine.stopIndexMotor()
                    magazine.stopRejectMotor()
                },
                magazine
            )
        }

        fun Definition.ejectAll(): Command {
            return ejectFeeder()
                .withTimeout(1.0)
                .andThen(ejectIndex().withTimeout(1.0))
                .alongWith(eject().withTimeout(1.0))
        }
    }

    object ShooterCommands {
        fun Definition.shoot(
            velocity: () -> Double,
            mode: () -> Shoot.Mode
        ): Shoot {
            return Shoot(
                shooter = subsystems.shooter,
                magazine = subsystems.magazine,
                targetVelocity = velocity,
                mode = mode,
                maxReloadTicks = 50,
                maxShooterDelay = 18
            )
        }
    }


    object ClimberCommands {
        fun Definition.climb(): Climb {
            return Climb(
                climber = subsystems.climber
            )
        }
    }

    object TestCommands {
        fun MotorController.runMotor(velocity: () -> Double): Command {
            return StartEndCommand(
                /* onInit = */ { set(velocity()) },
                /* onEnd = */ { stopMotor() }
            )
        }

        fun Definition.shooterFlow(velocity: () -> Double): Command {
            return with(subsystems) {
                StartEndCommand(
                    {
                        shooter.speed = velocity()
                        intake.start()
                        magazine.startIndexMotor()
                        magazine.startFeederMotor()
                        magazine.reverseRejectMotor()
                    },
                    {
                        shooter.stop()
                        intake.stop()
                        magazine.stopIndexMotor()
                        magazine.stopFeederMotor()
                        magazine.stopRejectMotor()
                    },
                    intake, magazine, shooter
                )
            }
        }

        fun Definition.rejectFlow(): Command {
            return with(subsystems) {
                StartEndCommand(
                    {
                        intake.start()
                        magazine.startIndexMotor()
                        magazine.startRejectMotor()
                    },
                    {
                        intake.stop()
                        magazine.stopIndexMotor()
                        magazine.stopRejectMotor()
                    },
                    intake, magazine
                )
            }
        }
    }
}