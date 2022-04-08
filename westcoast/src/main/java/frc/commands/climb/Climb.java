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
    private final double deployGoal = 3; //Make this value line up to motors
    private final double finalGoal = 6; //and this one
    private State state = State.Deploying;

    public Climb(
        @NotNull ClimberSubsystem climber
    ) {
        this.climber = climber;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.resetEncoder();
        state = State.Deploying;
    }

    @Override
    public void execute() {
        switch (state) {
            case End:
                climber.stop();
                break;
            case Deploying:
                deploy();
                break;
            case Climbing:

                break;
        }
    }

    private void deploy() {
        if (climber.getPosition() >= deployGoal) {
            state = State.Climbing;
        }
        climber.setPosition(deployGoal);
    }

    private void climb() {
        if (climber.getPosition() >= finalGoal) {
            state = State.End;
        }
        climber.setPosition(finalGoal);
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
