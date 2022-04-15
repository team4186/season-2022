package frc.robot

import edu.wpi.first.cameraserver.CameraServer
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.commands.Autonomous.move
import frc.commands.Autonomous.outPickInShootTwice
import frc.commands.Autonomous.shootAndLeave
import frc.commands.Autonomous.shootLeaveAndCollect
import frc.commands.Autonomous.shootOutPickInShootOut
import frc.commands.Commands.AutonomousDriveCommands.setupShot
import frc.commands.Commands.ClimberCommands.climb
import frc.commands.Commands.DriveCommands.cheesy
import frc.commands.Commands.DriveCommands.raw
import frc.commands.Commands.IntakeCommands.collect
import frc.commands.Commands.IntakeCommands.deploy
import frc.commands.Commands.IntakeCommands.retrieve
import frc.commands.Commands.MagazineCommands.ejectAll
import frc.commands.Commands.MagazineCommands.ejectIndex
import frc.commands.Commands.ShooterCommands.shoot
import frc.commands.magazine.Shoot
import frc.robot.definition.Definition
import frc.subsystems.MagazineSubsystem

class Robot(private val definition: Definition) : TimedRobot() {
    private enum class DriveMode {
        Raw,
        Cheesy
    }

    private var shooterMode = Shoot.Mode.Single
    private val autonomousChooser = SendableChooser<Command>()
    private val driveModeChooser = SendableChooser<DriveMode>()
    private var chosenColor: MagazineSubsystem.Target = MagazineSubsystem.Target.Red
    private val sendDebug = true
    override fun robotInit() {
        CameraServer.startAutomaticCapture()
        definition.subsystems.driveTrain.initialize()

        with(autonomousChooser) {
            addOption("LeaveTarmac", definition.move(2.0))
            addOption("Shoots and Leaves", definition.shootAndLeave(AUTONOMOUS_SHOOT_SPEED))

            val collectTeamColor = { definition.subsystems.magazine.isMatchingColor(chosenColor) }

            addOption(
                "Shoot Pick Shoot Leaves",
                definition.shootOutPickInShootOut(collectTeamColor, AUTONOMOUS_SHOOT_SPEED)
            )
            addOption(
                "Shoot Leave and Collect",
                definition.shootLeaveAndCollect(collectTeamColor, AUTONOMOUS_SHOOT_SPEED)
            )
            setDefaultOption(
                "Pick Shoot 2x Leaves",
                definition.outPickInShootTwice(collectTeamColor, AUTONOMOUS_SHOOT_SPEED)
            )

            SmartDashboard.putData("Autonomous Mode", this)
        }

        with(driveModeChooser) {
            setDefaultOption("Raw", DriveMode.Raw)
            addOption("Cheesy", DriveMode.Cheesy)
            SmartDashboard.putData("Drive Mode", this)
        }
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
        if (sendDebug) {
            // NOTE NaN causing sim to crash
            SmartDashboard.putNumber("Limelight Distance", definition.sensors.drive.vision.distance)
            SmartDashboard.putNumber("Left Encoder", definition.subsystems.driveTrain.leftEncoder.distance)
            SmartDashboard.putNumber("Right Encoder", definition.subsystems.driveTrain.rightEncoder.distance)
            SmartDashboard.putNumber("Climber Encoder", definition.subsystems.climber.position)
            definition.subsystems.shooter.periodic()
            definition.sensors.drive.vision.periodic()
        }
    }

    override fun autonomousInit() {
        chosenColor = when (DriverStation.getAlliance()) {
            DriverStation.Alliance.Blue -> MagazineSubsystem.Target.Blue
            DriverStation.Alliance.Red -> MagazineSubsystem.Target.Red
            else -> MagazineSubsystem.Target.Red
        }
        val autonomous = autonomousChooser.selected
        autonomous?.schedule()
    }

    override fun autonomousPeriodic() {
        accelerateShooter(AUTONOMOUS_SHOOT_SPEED)
    }

    override fun autonomousExit() {
        CommandScheduler.getInstance().cancelAll()
    }

    override fun teleopInit() {
        definition.sensors.drive.vision.setLight(mode = true)
        definition
            .subsystems
            .climber
            .resetEncoder()

        when (driveModeChooser.selected) {
            DriveMode.Cheesy -> definition.cheesy()
            DriveMode.Raw -> definition.raw()
            else -> definition.raw()
        }.schedule()

        definition
            .input
            .deployIntake
            .whenPressed(definition.deploy())

        definition
            .input
            .retrieveIntake
            .whenPressed(definition.retrieve())

        val teleopChosenColor = when (DriverStation.getAlliance()) {
            DriverStation.Alliance.Blue -> MagazineSubsystem.Target.Blue
            DriverStation.Alliance.Red -> MagazineSubsystem.Target.Red
            else -> MagazineSubsystem.Target.Red
        }

        definition
            .input
            .collect
            .whileActiveOnce(
                definition.collect {
                    definition.subsystems.magazine.isMatchingColor(teleopChosenColor)
                }
            )

        definition
            .input
            .shooterMode
            .whenPressed(Runnable {
                shooterMode = when (shooterMode) {
                    Shoot.Mode.Single -> Shoot.Mode.Full
                    Shoot.Mode.Full -> Shoot.Mode.Single
                }
            })

        definition
            .input
            .shoot
            .whileActiveOnce(definition.shoot(
                velocity = { shooterSpeed },
                mode = { shooterMode }
            ))

        definition
            .input
            .collectIgnoreColor
            .whileActiveOnce(definition.collect { true })

        definition
            .input
            .climb
            .whenPressed(definition.climb())

        // DEBUG BUTTONS
        definition
            .input
            .rejectAll
            .whileActiveOnce(definition.ejectAll())

        definition
            .input
            .rejectIndex
            .whileActiveOnce(definition.ejectIndex())

        definition
            .input
            .shooterSpeedFast
            .whileHeld(
                definition.setupShot { Units.inchesToMeters(50.0) }
            )
    }

    override fun teleopPeriodic() {
        if (sendDebug) {
            SmartDashboard.putNumber("Shooter Speed", definition.motors.shooter.lead.encoder.velocity)
            SmartDashboard.putNumber("Left Drive Speed", definition.sensors.drive.leftEncoder.rate)
            SmartDashboard.putNumber("Right Drive Speed", definition.sensors.drive.rightEncoder.rate)
            SmartDashboard.putBoolean("Feeder", definition.sensors.magazine.feeder.get())
            SmartDashboard.putBoolean("Index", definition.sensors.magazine.index.get())
            SmartDashboard.putBoolean("Reject", definition.sensors.magazine.reject.get())
            SmartDashboard.putBoolean("Color Match", definition.subsystems.magazine.isMatchingColor(chosenColor))
            val color = definition.sensors.magazine.colorSensor.color
            SmartDashboard.putString("Color", String.format("Color(%f, %f, %f)", color.red, color.green, color.blue))
        }
        accelerateShooter(shooterSpeed)
    }

    override fun teleopExit() {
        definition.sensors.drive.vision.setLight(mode = false)
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testInit() {
        definition.subsystems.driveTrain.rightEncoder.reset()
        definition.subsystems.driveTrain.leftEncoder.reset()
    }

    var shootFinished = 0
    private fun accelerateShooter(speed: Double) {
        val magazine = definition.subsystems.magazine
        if (magazine.hasFeederSensorBreak() || magazine.hasIndexSensorBreak()) {
            definition.subsystems.shooter.speed = speed
            shootFinished = 0
        } else {
            definition.subsystems.shooter.speed = speed
            shootFinished++
        }
        if (shootFinished > 50) {
            definition.subsystems.shooter.stop()
        }
    }

    private val shooterSpeed: Double
        get() = (definition.input.joystick.z - 1.0) * 0.5 * -1.0 * 1500.0 + 3500.0

    companion object {
        private const val AUTONOMOUS_SHOOT_SPEED = 3500.0
    }
}