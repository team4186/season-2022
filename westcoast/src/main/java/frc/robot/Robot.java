package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsControlModule;
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

        definition
                .input
                .collect
                .whileActiveOnce(collect(
                        definition,
                        () -> {
                            Color color = colorChooser.getSelected();

                            if (color == null) {
                                color = MagazineSubsystem.RedTarget;
                            }
                            return color;
                        }
                ));

        definition
                .input
                .shoot
                .whileActiveOnce(shoot(
                        definition,
                        () -> (definition.input.joystick.getZ() + 1.0) * 0.5 * 5676.0
                ));
    }

    @Override
    public void teleopPeriodic() {
//        SmartDashboard.putBoolean("Feeder", definition.sensors.magazine.feeder.get());
//        SmartDashboard.putBoolean("Index", definition.sensors.magazine.index.get());
//        SmartDashboard.putBoolean("Reject", definition.sensors.magazine.reject.get());
//        SmartDashboard.putBoolean("Color Match", definition.subsystems.magazine.isMatchingColor(colorChooser.getSelected()));
    }

    @Override
    public void teleopExit() {
        CommandScheduler.getInstance().cancelAll();
    }
}