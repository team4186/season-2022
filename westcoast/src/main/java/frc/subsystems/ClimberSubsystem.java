package frc.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.definition.Controllers;
import org.jetbrains.annotations.NotNull;

public class ClimberSubsystem extends SubsystemBase {
    @NotNull
    private final CANSparkMax climberMotor;
    private final SparkMaxPIDController pidController;
    @NotNull
    private final Controllers.ControllerConfigurator climberConfigDeploy;
    @NotNull
    private final Controllers.ControllerConfigurator climberConfigClimb;


    DigitalInput limitSwitchRight = new DigitalInput(3);
    DigitalInput limitSwitchLeft = new DigitalInput(5);
    // don't think I have to put this in ShinDestroyer but maybe
    // also this might be the issue someone check this plz

    // final private members
    private boolean isClimbing = false;

    public ClimberSubsystem(
            @NotNull CANSparkMax climberMotor,
            @NotNull Controllers.ControllerConfigurator climberConfigDeploy,
            @NotNull Controllers.ControllerConfigurator climberConfigClimb) {
        this.climberMotor = climberMotor;
        pidController = climberMotor.getPIDController();
        this.climberConfigDeploy = climberConfigDeploy;
        this.climberConfigClimb = climberConfigClimb;
        climberConfigDeploy.configure(pidController);
    }

    public double getPosition() {
        return climberMotor.getEncoder().getPosition();
    }

    public void resetEncoder() {
        climberMotor.getEncoder().setPosition(0);
    }

    public boolean isLimit() {
        return (limitSwitchLeft.get() || limitSwitchRight.get());
    }

    public boolean getRightLimit(){
        //System.out.println("RIGHT:" + limitSwitchRight.get());
        return limitSwitchRight.get();
    }

    public boolean getLeftLimit(){
        //System.out.println("LEFT:" + limitSwitchLeft.get());
        return limitSwitchLeft.get();
    }

    public void setPosition(double position) {
        pidController.setReference(position, CANSparkMax.ControlType.kPosition);
    }

    public void setClimberConfigDeploy() {
        if (isClimbing) {
            climberConfigDeploy.configure(pidController);
            isClimbing = false;
        }
    }

    public void setClimberConfigClimb() {
        if (!isClimbing) {
            climberConfigClimb.configure(pidController);
            isClimbing = true;
        }
    }

    public void stop() {
        climberMotor.stopMotor();
    }

    @Override
    public void periodic() {
    }
}