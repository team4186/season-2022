package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.commands.Autonomous;
import frc.commands.Commands;
import frc.robot.definition.Definition;
import frc.subsystems.MagazineSubsystem;
import frc.subsystems.ShooterSubsystem;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.IntakeCommands.*;
import static frc.commands.Commands.ShooterCommands.shoot;

public class Robot extends TimedRobot {

    @NotNull
    private final Definition definition;
    @NotNull
    private final SendableChooser<Command> autonomousChooser = new SendableChooser<>();
    private Color chosenColor = MagazineSubsystem.RedTarget;

    private final boolean sendDebug = false;

    public Robot(@NotNull Definition definition) {
        this.definition = definition;
    }

    @Override
    public void robotInit() {
        definition.subsystems.driveTrain.initialize();

        autonomousChooser.addOption("LeaveTarmac", Autonomous.move(320, definition));
        autonomousChooser.addOption("Shoots and Leaves", Autonomous.shootAndLeave(definition));
        SmartDashboard.putData("Autonomous Mode", autonomousChooser);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        if (sendDebug) {
            SmartDashboard.putNumber("Left Encoder", definition.subsystems.driveTrain.leftEncoder.get());
            SmartDashboard.putNumber("Right Encoder", definition.subsystems.driveTrain.rightEncoder.get());
        }
    }

    @Override
    public void autonomousInit() {
        Command autonomous = autonomousChooser.getSelected();
        if (autonomous != null) {
            autonomous.schedule();
        }
    }

    @Override
    public void autonomousExit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void teleopInit() {
        Commands
                .TeleopCommands
                .encodedAssisted(definition)
                .schedule();

//        Commands.IntakeCommands.deploy(definition).schedule();

        definition
                .input
                .deployIntake
                .whenPressed(deploy(definition));

        definition
                .input
                .retrieveIntake
                .whenPressed(retrieve(definition));

        if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
            chosenColor = MagazineSubsystem.BlueTarget;
        } else {
            chosenColor = MagazineSubsystem.RedTarget;
        }

        definition
                .input
                .collect
                .whileActiveOnce(collect(definition, () -> chosenColor));

        definition
                .input
                .shoot
                .whileActiveOnce(shoot(
                        definition,
                        () -> 3100.0
                ));

        definition
                .input
                .runIndexMotor
                .whileActiveOnce(Commands.TestCommands.runMotor(
                        definition.motors.shooter.lead,
                        () -> 3093 / ShooterSubsystem.MAX_SPEED
                ));

        definition
                .input
                .runRejectMotor
                .whileActiveOnce(
                        new StartEndCommand(
                                () -> definition.subsystems.shooter.setSpeed(definition.input.joystick.getZ() * ShooterSubsystem.MAX_SPEED),
                                definition.subsystems.shooter::stop
                        )
                );
    }

    @Override
    public void teleopPeriodic() {
        if (sendDebug) {
            SmartDashboard.putBoolean("Feeder", definition.sensors.magazine.feeder.get());
            SmartDashboard.putBoolean("Index", definition.sensors.magazine.index.get());
            SmartDashboard.putBoolean("Reject", definition.sensors.magazine.reject.get());
            SmartDashboard.putBoolean("Color Match", definition.subsystems.magazine.isMatchingColor(chosenColor));
            Color color = definition.sensors.magazine.colorSensor.getColor();
            SmartDashboard.putString("Color", String.format("Color(%f, %f, %f)", color.red, color.green, color.blue));
        }
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
}