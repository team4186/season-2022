package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.commands.Autonomous;
import frc.commands.Commands;
import frc.commands.magazine.Shoot;
import frc.robot.definition.Definition;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.ClimberCommands.climb;
import static frc.commands.Commands.IntakeCommands.*;
import static frc.commands.Commands.ShooterCommands.shoot;

public class Robot extends TimedRobot {

    private enum DriveMode {
        Raw,
        Cheesy
    }

    private static final double SHOOTER_SPEED_SLOW = 3500;
    private static final double SHOOTER_SPEED_FAST = 3800;
    private double shooterSpeed = SHOOTER_SPEED_SLOW;
    private Shoot.Mode shooterMode = Shoot.Mode.Single;

    @NotNull
    private final Definition definition;
    @NotNull
    private final SendableChooser<Command> autonomousChooser = new SendableChooser<>();
    @NotNull
    private final SendableChooser<DriveMode> driveModeChooser = new SendableChooser<>();

    private Color chosenColor = MagazineSubsystem.RedTarget;

    private final boolean sendDebug = true;

    public Robot(@NotNull Definition definition) {
        this.definition = definition;
    }

    @Override
    public void robotInit() {

        CameraServer.startAutomaticCapture();

        definition.subsystems.driveTrain.initialize();

        autonomousChooser.addOption("LeaveTarmac", Autonomous.move(definition, 2.0));
        autonomousChooser.addOption("Shoots and Leaves", Autonomous.shootAndLeave(definition));
        autonomousChooser.addOption("Shoot Pick Shoot Leaves", Autonomous.shootOutPickInShootOut(definition, () -> definition.subsystems.magazine.isMatchingColor(chosenColor)));
        autonomousChooser.addOption("Pick Shoot 2x Leaves", Autonomous.outPickInShootTwice(definition, () -> definition.subsystems.magazine.isMatchingColor(chosenColor)));
        SmartDashboard.putData("Autonomous Mode", autonomousChooser);

        driveModeChooser.setDefaultOption("Raw", DriveMode.Raw);
        driveModeChooser.addOption("Cheesy", DriveMode.Cheesy);
        SmartDashboard.putData("Drive Mode", driveModeChooser);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        if (sendDebug) {
            SmartDashboard.putNumber("Left Encoder", definition.subsystems.driveTrain.leftEncoder.getDistance());
            SmartDashboard.putNumber("Right Encoder", definition.subsystems.driveTrain.rightEncoder.getDistance());
            SmartDashboard.putNumber("Climber Encoder", definition.subsystems.climber.getPosition());

            definition.subsystems.shooter.periodic();
        }
    }

    @Override
    public void autonomousInit() {
        if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
            chosenColor = MagazineSubsystem.BlueTarget;
        } else {
            chosenColor = MagazineSubsystem.RedTarget;
        }


        Command autonomous = autonomousChooser.getSelected();
        if (autonomous != null) {
            autonomous.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
        accelerateShooter();
    }

    @Override
    public void autonomousExit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void teleopInit() {
        definition.subsystems.climber.resetEncoder();
        switch (driveModeChooser.getSelected()) {
            case Cheesy:
                Commands
                        .TeleopCommands
                        .cheesy(definition)
                        .schedule();
                break;

            default:
            case Raw:
                Commands
                        .TeleopCommands
                        .raw(definition)
                        .schedule();
                break;
        }

        definition
                .input
                .deployIntake
                .whenPressed(deploy(definition));

        definition
                .input
                .retrieveIntake
                .whenPressed(retrieve(definition));


        final Color teleopChosenColor;
        if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
            teleopChosenColor = MagazineSubsystem.BlueTarget;
        } else {
            teleopChosenColor = MagazineSubsystem.RedTarget;
        }

        definition
                .input
                .collect
                .whileActiveOnce(collect(
                        definition,
                        () -> definition.subsystems.magazine.isMatchingColor(teleopChosenColor)
                ));


        definition
                .input
                .shooterSpeedSlow
                .whenPressed(() -> shooterSpeed = SHOOTER_SPEED_SLOW);

        definition
                .input
                .shooterSpeedFast
                .whenPressed(() -> shooterSpeed = SHOOTER_SPEED_FAST);

        definition
                .input
                .shooterMode
                .whenPressed(() -> {
                    switch (shooterMode) {
                        case Single:
                            shooterMode = Shoot.Mode.Full;
                            break;
                        case Full:
                            shooterMode = Shoot.Mode.Single;
                            break;
                    }
                });

        definition
                .input
                .shoot
                .whileActiveOnce(shoot(
                        definition,
                        this::getShooterSpeed,
                        () -> shooterMode
                ));


        definition
                .input
                .collectIgnoreColor
                .whileActiveOnce(collect(
                        definition,
                        () -> true
                ));

        definition
                .input
                .climb
                .whileActiveOnce(climb(
                        definition
                ));

        // DEBUG BUTTONS
        definition
                .input
                .rejectAll
                .whileActiveOnce(Commands.MagazineCommands.ejectAll(definition));

        definition
                .input
                .rejectIndex
                .whileActiveOnce(Commands.MagazineCommands.ejectIndex(definition));
    }

    @Override
    public void teleopPeriodic() {
        if (sendDebug) {
            SmartDashboard.putNumber("Shooter Speed", definition.motors.shooter.lead.getEncoder().getVelocity());
            SmartDashboard.putNumber("Left Drive Speed", definition.sensors.drive.leftEncoder.getRate());
            SmartDashboard.putNumber("Right Drive Speed", definition.sensors.drive.rightEncoder.getRate());
            SmartDashboard.putBoolean("Feeder", definition.sensors.magazine.feeder.get());
            SmartDashboard.putBoolean("Index", definition.sensors.magazine.index.get());
            SmartDashboard.putBoolean("Reject", definition.sensors.magazine.reject.get());
            SmartDashboard.putBoolean("Color Match", definition.subsystems.magazine.isMatchingColor(chosenColor));
            Color color = definition.sensors.magazine.colorSensor.getColor();
            SmartDashboard.putString("Color", String.format("Color(%f, %f, %f)", color.red, color.green, color.blue));
        }
        accelerateShooter();
    }

    @Override
    public void teleopExit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testInit() {
        definition.subsystems.driveTrain.rightEncoder.reset();
        definition.subsystems.driveTrain.leftEncoder.reset();
    }

    int shootFinished = 0;

    private void accelerateShooter() {
        MagazineSubsystem magazine = definition.subsystems.magazine;
        if (magazine.hasFeederSensorBreak() || magazine.hasIndexSensorBreak()) {
            definition.subsystems.shooter.setSpeed(getShooterSpeed());
            shootFinished = 0;
        } else {
            definition.subsystems.shooter.setSpeed(getShooterSpeed());
            shootFinished++;
        }

        if (shootFinished > 50) {
            definition.subsystems.shooter.stop();
        }
    }

    private double getShooterSpeed() {
        return (definition.input.joystick.getZ() - 1.0) * 0.5 * -1.0 * 1500.0 + 3500.0;
    }
}