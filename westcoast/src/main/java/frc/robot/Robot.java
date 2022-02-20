package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.commands.Autonomous;
import frc.commands.Commands;
import frc.robot.definition.Definition;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.DriveCommands.alignToTarget;
import static frc.commands.Commands.DriveCommands.findTarget;

public class Robot extends TimedRobot {

    @NotNull
    private final Definition definition;
    @NotNull
    private final SendableChooser<Command> autonomousChooser = new SendableChooser<>();

    public Robot(@NotNull Definition definition) {
        this.definition = definition;
    }

    @Override
    public void robotInit() {
        definition.subsystems.driveTrain.initialize();

        autonomousChooser.setDefaultOption("Target", Autonomous.target(definition, -3.0));
        autonomousChooser.addOption("Center", Autonomous.center(definition, -3.0, -30.0));
        autonomousChooser.addOption("LoadingBay", Autonomous.loadingBay(definition, -3.0, -40.0));
        autonomousChooser.addOption(
                "SpinFast",
                new SequentialCommandGroup(
                        findTarget(definition),
                        alignToTarget(definition)
                )
        );
        autonomousChooser.addOption("Flex on Mr. Felipe", Autonomous.flex(definition));
        SmartDashboard.putData("Autonomous Mode", autonomousChooser);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        autonomousChooser
                .getSelected()
                .schedule();
    }

    @Override
    public void autonomousExit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void teleopInit() {
        Commands
                .TeleopCommands
                .raw(definition)
                .schedule();
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void teleopExit() {
        CommandScheduler.getInstance().cancelAll();
    }
}