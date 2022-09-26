package frc.commands.climb;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.ClimberSubsystem;
import frc.subsystems.MagazineSubsystem;
import frc.subsystems.ShooterSubsystem;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

public final class Climb extends CommandBase {

    private enum State {
        Deploying,
        Climbing,
        End
    }

    @NotNull
    private final ClimberSubsystem climber;
    private static final double deployGoal = 50; //Make this value line up to motors
    private static final double finalGoal = 130; //and this one (both are in terms of motor rotations)
    private State state = State.Deploying;

    public Climb(
        @NotNull ClimberSubsystem climber
    ) {
        this.climber = climber;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        if (climber.getPosition() < deployGoal - 8) { //tolerance for climber (5 revolutions is 1/2 a shaft rotation)
            state = State.Deploying;
        } else if (climber.getPosition() < finalGoal) {
            state = State.Climbing;
        } else {
            state = State.End;
        }
    }

    @Override
    public void execute() {
        switch (state) {
            case End:
                if (climber.getLeftLimit()){
                    climber.stopLeft();
                }
                if (climber.getRightLimit()){
                    climber.stopRight();
                }
                break;
            case Deploying:
                deploy();
                break;
            case Climbing:
                climb();
                break;
        }
    }

    private void deploy() {
        if (climber.getPosition() >= deployGoal) {
            state = State.End;
        } else if (climber.isLimit()){
            state = State.End;
            //this should end deploy if either of the limit switches are hit
        } else {
            climber.setClimberConfigDeploy();
            climber.setPosition(deployGoal);
        }
    }

    private void climb() {
        if (climber.getPosition() >= finalGoal) {
            state = State.End;
        } else if (climber.isLimit()){
             state = State.End;
             //this should end the climb if either of the limit switches are hit
        } else {
            climber.setClimberConfigClimb();
            climber.setPosition(finalGoal);
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }

    @Override
    public boolean isFinished() {
        return state == State.End;
    }
}
