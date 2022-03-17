package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.commands.Commands;
import frc.robot.definition.Definition;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.IntakeCommands.*;
import static frc.commands.Commands.ShooterCommands.ignoreSensors;
import static frc.commands.Commands.ShooterCommands.shoot;

public class Robot extends TimedRobot {

    @NotNull
    private final Definition definition;
    @NotNull
    private final SendableChooser<Command> autonomousChooser = new SendableChooser<>();
    @NotNull
    private final SendableChooser<Color> colorChooser = new SendableChooser<>();

    public Robot(@NotNull Definition definition) {
        this.definition = definition;
    }

    @Override
    public void robotInit() {
        definition.subsystems.driveTrain.initialize();
        SmartDashboard.putData("Autonomous Mode", autonomousChooser);


        colorChooser.addOption("Blue", MagazineSubsystem.BlueTarget);
        colorChooser.setDefaultOption("Red", MagazineSubsystem.RedTarget);
        SmartDashboard.putData("Cargo Color", colorChooser);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
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
//        Commands
//                .TeleopCommands
//                .encodedAssisted(definition)
//                .schedule();
//
//        definition
//                .input
//                .deployIntake
//                .whenPressed(deploy(definition));
//
//        definition
//                .input
//                .retrieveIntake
//                .whenPressed(retrieve(definition));
//
//        Color color = colorChooser.getSelected();
//
//        if (color == null) {
//            color = MagazineSubsystem.RedTarget;
//        }
//
//        definition
//                .input
//                .collect
//                .whileHeld(collect(definition, color));
//
//
//        definition.input.shoot.whenPressed(ignoreSensors(definition));

    }

    @Override
    public void teleopPeriodic() {

        SmartDashboard.putBoolean("Color", definition.subsystems.magazine.isMatchingColor(MagazineSubsystem.RedTarget));
//        if(definition.input.collect.get()) {
//            definition.motors.intake.main.set(0.5);
//        }
//        else {
//            definition.motors.intake.main.stopMotor();
//        }
//        if(definition.input.deployIntake.get()) {
//            definition.motors.magazine.feeder.set(-0.5);
//        } else {
//            definition.motors.magazine.feeder.stopMotor();
//        }
//        if(definition.input.retrieveIntake.get()) {
//            definition.motors.magazine.index.set(-0.5);
//        }else {
//            definition.motors.magazine.index.stopMotor();
//        }
//        if(definition.input.turnInPlace.get()) {
//            definition.motors.magazine.reject.set(0.5);
//        }
//        else {
//            definition.motors.magazine.reject.stopMotor();
//        }
//        if(definition.input.shoot.get()) {
//            definition.motors.shooter.lead.set(0.5);
//        }
//        else {
//            definition.motors.shooter.lead.stopMotor();
//        }
    }

    @Override
    public void teleopExit() {
        CommandScheduler.getInstance().cancelAll();
    }
}