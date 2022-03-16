package frc.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.MagazineSubsystem;
import frc.subsystems.ShooterSubsystem;
import org.jetbrains.annotations.NotNull;

public final class Shoot extends CommandBase {

    private enum State {
        End,
        Reloading,
        Accelerating,
        Shooting
    }

    @NotNull
    private final ShooterSubsystem shooter;
    @NotNull
    private final MagazineSubsystem magazine;

    private final int maxReloadTicks;
    private final double minShooterVelocity;

    private State state = State.Reloading;

    public Shoot(
            @NotNull ShooterSubsystem shooter,
            @NotNull MagazineSubsystem magazine,
            int maxReloadTicks,
            double minShooterVelocity
    ) {
        this.shooter = shooter;
        this.magazine = magazine;
        this.maxReloadTicks = maxReloadTicks;
        this.minShooterVelocity = minShooterVelocity;

        addRequirements(shooter, magazine);
    }

    @Override
    public void initialize() {
        state = State.Reloading;
        reloadTimeout = maxReloadTicks;
    }

    @Override
    public void execute() {
        switch (state) {
            case End:
                shooter.stop();
                break;
            case Reloading:
                reloading();
                // Early evaluation just in case the magazine is empty right at the first frame
                if (state != State.End) {
                    shooter.setSpeed(minShooterVelocity);
                }
                break;
            case Accelerating:
                accelerating();
                shooter.setSpeed(minShooterVelocity);
                break;
            case Shooting:
                shooting();
                shooter.setSpeed(minShooterVelocity);
                break;
        }
    }

    private int reloadTimeout = 0;

    private void reloading() {
        if (magazine.hasFeederSensorBreak()) {
            state = State.Accelerating;
            reloadTimeout = 0;
        } else if (magazine.hasIndexSensorBreak()) {
            magazine.startIndexMotor();
            magazine.startFeederMotor();
            reloadTimeout = 0;
        } else if (reloadTimeout++ >= maxReloadTicks) {
            magazine.stopIndexMotor();
            magazine.stopFeederMotor();
            state = State.End;
        }
    }

    private void accelerating() {
        if (!magazine.hasFeederSensorBreak()) {
            state = State.Reloading;
        } else if (shooter.getSpeed() >= minShooterVelocity) {
            state = State.Shooting;
        }
    }

    private void shooting() {
        if (!magazine.hasFeederSensorBreak()) {
            state = State.Reloading;
        } else if (shooter.getSpeed() < minShooterVelocity) {
            state = State.Accelerating;
        } else {
            magazine.startIndexMotor();
            magazine.startFeederMotor();
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
        magazine.stopIndexMotor();
        magazine.stopFeederMotor();
    }

    @Override
    public boolean isFinished() {
        return state == State.End;
    }
}
