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
        Commands
                .TeleopCommands
                .encodedAssisted(definition)
                .schedule();

        definition
                .input
                .deployIntake
                .whenPressed(deploy(definition));

        definition
                .input
                .retrieveIntake
                .whenPressed(retrieve(definition));

        Color color = colorChooser.getSelected();

        if (color == null) {
            color = MagazineSubsystem.RedTarget;
        }

        definition
                .input
                .collect
                .whileHeld(collect(definition, color));

        definition
                .input
                .shoot
                .whenPressed(shoot(
                        definition,
                        () -> (definition.input.joystick.getThrottle() + 1.0) * 0.5 * 5676.0
                ));

        definition
                .input
                .runIntakeMotor
                .whileActiveOnce(Commands.TestCommands.runMotor(
                        definition.motors.intake.main,
                        definition.input.joystick::getThrottle
                ));

        definition
                .input
                .runIndexMotor
                .whileActiveOnce(Commands.TestCommands.runMotor(
                        definition.motors.magazine.index,
                        definition.input.joystick::getThrottle
                ));

        definition
                .input
                .runFeederMotor
                .whileActiveOnce(Commands.TestCommands.runMotor(
                        definition.motors.magazine.feeder,
                        definition.input.joystick::getThrottle
                ));

        definition
                .input
                .runRejectMotor
                .whileActiveOnce(Commands.TestCommands.runMotor(
                        definition.motors.magazine.reject,
                        definition.input.joystick::getThrottle
                ));

        definition
                .input
                .runShooterMotor
                .whileActiveOnce(Commands.TestCommands.runMotor(
                        definition.motors.shooter.lead,
                        definition.input.joystick::getThrottle
                ));
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void teleopExit() {
        CommandScheduler.getInstance().cancelAll();
    }
}