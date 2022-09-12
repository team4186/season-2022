package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.util.Units;
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
import frc.vision.VisionRunner;
import org.jetbrains.annotations.NotNull;

import static frc.commands.Commands.ClimberCommands.climb;
import static frc.commands.Commands.IntakeCommands.*;
import static frc.commands.Commands.ShooterCommands.shoot;

public class Robot extends TimedRobot {

    private enum DriveMode {
        Raw,
        Cheesy
    }

    private static final double AUTONOMOUS_SHOOT_SPEED = 3500;
    private Shoot.Mode shooterMode = Shoot.Mode.Single;

    @NotNull
    private final Definition definition;
    @NotNull
    private final SendableChooser<Command> autonomousChooser = new SendableChooser<>();
    @NotNull
    private final SendableChooser<DriveMode> driveModeChooser = new SendableChooser<>();

    private Color chosenColor = MagazineSubsystem.RedTarget;

    private final boolean sendDebug = true;

    private VisionRunner limelight;

    public Robot(@NotNull Definition definition) {
        this.definition = definition;
    }

    @Override
    public void robotInit() {
        CameraServer.startAutomaticCapture();

        limelight = definition.subsystems.driveTrain.vision;

        definition.subsystems.driveTrain.initialize();

        autonomousChooser.addOption("LeaveTarmac", Autonomous.move(definition, 2.0));
        autonomousChooser.addOption("Shoots and Leaves", Autonomous.shootAndLeave(definition, AUTONOMOUS_SHOOT_SPEED));
        autonomousChooser.addOption("Shoots and Leaves Vision", Autonomous.shootAndLeaveVision(definition, AUTONOMOUS_SHOOT_SPEED));
        autonomousChooser.addOption("Shoot Pick Shoot Leaves", Autonomous.shootOutPickInShootOut(definition, () -> definition.subsystems.magazine.isMatchingColor(chosenColor), AUTONOMOUS_SHOOT_SPEED));
        autonomousChooser.addOption("Pick Shoot 2x Leaves", Autonomous.outPickInShootTwice(definition, () -> definition.subsystems.magazine.isMatchingColor(chosenColor), AUTONOMOUS_SHOOT_SPEED));
        autonomousChooser.addOption("Shoot Leave and Collect", Autonomous.shootLeaveAndCollect(definition, () -> definition.subsystems.magazine.isMatchingColor(chosenColor), AUTONOMOUS_SHOOT_SPEED));
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
            limelight.periodic();
        }
    }

    @Override
    public void autonomousInit() {
        limelight.setLight(true);

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
        accelerateShooter(AUTONOMOUS_SHOOT_SPEED);
    }

    @Override
    public void autonomousExit() {
        CommandScheduler.getInstance().cancelAll();
        limelight.setLight(false);
    }

    @Override
    public void teleopInit() {
        limelight.setLight(true);

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
                .whenPressed(climb(
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

        // shooter set speed is 3670 for aimbotted shots (if we want a button that does that)
        definition
                .input
                .shooterSpeedFast
                .whileHeld(
                        Commands.DriveCommands.setupShot(
                                definition,
                                Units.inchesToMeters(55))
                );
    }

    @Override
    public void teleopPeriodic() {
        if (sendDebug) {
            SmartDashboard.putNumber("Shooter Speed", definition.motors.shooter.lead.getEncoder().getVelocity());
            SmartDashboard.putNumber("Shooter Set Speed", getShooterSpeed());
            SmartDashboard.putNumber("Left Drive Speed", definition.sensors.drive.leftEncoder.getRate());
            SmartDashboard.putNumber("Right Drive Speed", definition.sensors.drive.rightEncoder.getRate());
            SmartDashboard.putBoolean("Feeder", definition.sensors.magazine.feeder.get());
            SmartDashboard.putBoolean("Index", definition.sensors.magazine.index.get());
            SmartDashboard.putBoolean("Reject", definition.sensors.magazine.reject.get());
            SmartDashboard.putBoolean("Color Match", definition.subsystems.magazine.isMatchingColor(chosenColor));
            Color color = definition.sensors.magazine.colorSensor.getColor();
            SmartDashboard.putString("Color", String.format("Color(%f, %f, %f)", color.red, color.green, color.blue));
        }
        accelerateShooter(getShooterSpeed());
    }

    @Override
    public void teleopExit() {
        limelight.setLight(false);
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testInit() {
        limelight.setLight(true);
    }

    @Override
    public void testExit() {
        limelight.setLight(false);
        CommandScheduler.getInstance().cancelAll();
    }

    int shootFinished = 0;

    private void accelerateShooter(double speed) {
        MagazineSubsystem magazine = definition.subsystems.magazine;
        if (magazine.hasFeederSensorBreak() || magazine.hasIndexSensorBreak()) {
            definition.subsystems.shooter.setSpeed(speed);
            shootFinished = 0;
        } else {
            definition.subsystems.shooter.setSpeed(speed);
            shootFinished++;
        }

        if (shootFinished > 50) {
            definition.subsystems.shooter.stop();
        }
    }

    private double getShooterSpeed() {
//        return (definition.input.joystick.getZ() - 1.0) * 0.5 * -1.0 * 1500.0 + 3500.0;
        return 3671.259850; // calibrated during testing (may need recal on the field)
    }
}