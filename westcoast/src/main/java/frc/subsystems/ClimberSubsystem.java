package frc.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.definition.Controllers;
import org.jetbrains.annotations.NotNull;

public class ClimberSubsystem extends SubsystemBase {
    @NotNull
    private final CANSparkMax climberMotor1;
    private final CANSparkMax climberMotor2;
    private final SparkMaxPIDController pidController;
    private final SparkMaxPIDController pidController2;
    @NotNull
    private final Controllers.ControllerConfigurator climberConfigDeploy;
    @NotNull
    private final Controllers.ControllerConfigurator climberConfigClimb;
    @NotNull
    private final Controllers.ControllerConfigurator climberConfigDeploy2;
    @NotNull
    private final Controllers.ControllerConfigurator climberConfigClimb2;


    // final private members
    private boolean isClimbing = false;

    DigitalInput limitSwitchRight = new DigitalInput(3);
    DigitalInput limitSwitchLeft = new DigitalInput(5);

    public ClimberSubsystem(
            @NotNull CANSparkMax climberMotor1,
            @NotNull CANSparkMax climberMotor2,
            @NotNull Controllers.ControllerConfigurator climberConfigDeploy,
            @NotNull Controllers.ControllerConfigurator climberConfigDeploy2,
            @NotNull Controllers.ControllerConfigurator climberConfigClimb,
            @NotNull Controllers.ControllerConfigurator climberConfigClimb2) {
        this.climberMotor1 = climberMotor1;
        this.climberMotor2 = climberMotor2;
        pidController = climberMotor1.getPIDController();
        pidController2 = climberMotor2.getPIDController();
        this.climberConfigDeploy = climberConfigDeploy;
        this.climberConfigDeploy2 = climberConfigDeploy2;
        this.climberConfigClimb = climberConfigClimb;
        this.climberConfigClimb2 = climberConfigClimb2;
        climberConfigDeploy.configure(pidController);
    }

    public double getLeftPos() {
        return climberMotor1.getEncoder().getPosition();
    }

    public double getRightPos() { return climberMotor2.getEncoder().getPosition(); }

    public void resetEncoder() {
        climberMotor1.getEncoder().setPosition(0);
        climberMotor2.getEncoder().setPosition(0);
    }

    public boolean isLimit() {
        return (!limitSwitchLeft.get() || !limitSwitchRight.get());
    }

    public boolean getRightLimit(){
        return !limitSwitchRight.get();
    }

    public boolean getLeftLimit(){
        return !limitSwitchLeft.get();
    }

    public void setPosition(double position) {
        pidController.setReference(position, CANSparkMax.ControlType.kPosition);
        pidController2.setReference(position, CANSparkMax.ControlType.kPosition);
    }

    public void setClimberConfigDeploy() {
        if (isClimbing) {
            climberConfigDeploy.configure(pidController);
            climberConfigDeploy2.configure(pidController2);
            isClimbing = false;
        }
    }

    public void setClimberConfigClimb() {
        if (!isClimbing) {
            climberConfigClimb.configure(pidController);
            climberConfigClimb2.configure(pidController2);
            isClimbing = true;
        }
    }

    public void stop() {
        climberMotor1.stopMotor();
        climberMotor2.stopMotor();
    }

    public void stopLeft() {
        climberMotor1.stopMotor();
    }

    public void stopRight() {
        climberMotor2.stopMotor();
    }

    @Override
    public void periodic() {
    }
}