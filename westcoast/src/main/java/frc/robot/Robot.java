// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.subsystems.drive.TeleopDrive;
import frc.subsystems.drive.motorfactory.MotorFactory;
import frc.subsystems.drive.motorfactory.MotorFactoryHybrid;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
    MotorFactory hybridFactory = new MotorFactoryHybrid();
    private final MotorController leftMain = hybridFactory.create(14, 13, 15);
    private final MotorController rightMain = hybridFactory.create(2, 1, 3);
    private final DifferentialDrive drive = new DifferentialDrive(leftMain, rightMain);

    private final Joystick joystick = new Joystick(0);
    private final TeleopDrive teleop = new TeleopDrive(drive, joystick);

    /**
     * This method is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        drive.setSafetyEnabled(false);
    }
    
    /**
     * This method is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic methods, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {}
    
    /** This method is called once when teleop is enabled. */
    @Override
    public void teleopInit() {
        teleop.cancel();
        teleop.schedule();
    }
    
    
    /** This method is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
