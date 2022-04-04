package frc.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.MagazineSubsystem;
import frc.subsystems.ShooterSubsystem;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

public final class Shoot extends CommandBase {

    private enum State {
        End,
        Reloading,
        Accelerating,
        Shooting
    }

    public enum Mode {
        Single,
        Full
    }

    public interface ModeProvider {
        Mode getMode();
    }

    @NotNull
    private final ShooterSubsystem shooter;
    @NotNull
    private final MagazineSubsystem magazine;
    @NotNull
    private final DoubleSupplier targetVelocity;

    @NotNull
    private final ModeProvider mode;

    private final int maxReloadTicks;
    private final int maxShooterDelay;

    private static final int SPEED_TOLERANCE = 10;

    private State state = State.Reloading;

    public Shoot(
            @NotNull ShooterSubsystem shooter,
            @NotNull MagazineSubsystem magazine,
            @NotNull DoubleSupplier targetVelocity,
            @NotNull ModeProvider mode,
            int maxReloadTicks,
            int maxShooterDelay
    ) {
        this.shooter = shooter;
        this.magazine = magazine;
        this.targetVelocity = targetVelocity;
        this.mode = mode;
        this.maxReloadTicks = maxReloadTicks;
        this.maxShooterDelay = maxShooterDelay;

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
                shooterDelay = 0;
                reloading();
                // Early evaluation just in case the magazine is empty right at the first frame
                if (state != State.End) {
                    shooter.setSpeed(targetVelocity.getAsDouble());
                }
                break;
            case Accelerating:
                shooterDelay = 0;
                accelerating();
                shooter.setSpeed(targetVelocity.getAsDouble());
                break;
            case Shooting:
                shooting();
                shooter.setSpeed(targetVelocity.getAsDouble());
                break;
        }
    }

    private int reloadTimeout = 0;

    private void reloading() {
        if (magazine.hasFeederSensorBreak()) {
            magazine.stopIndexMotor();
            magazine.stopRejectMotor();
            magazine.stopFeederMotor();
            state = State.Accelerating;
            reloadTimeout = 0;
        } else if (magazine.hasIndexSensorBreak()) {
            magazine.startFeederMotor();
            magazine.startIndexMotor();
            magazine.reverseRejectMotor();
            reloadTimeout = 0;
        } else if (reloadTimeout++ >= maxReloadTicks) {
            magazine.stopIndexMotor();
            magazine.stopRejectMotor();
            magazine.stopFeederMotor();
            state = State.End;
        }
    }

    private void accelerating() {
        if (!magazine.hasFeederSensorBreak()) {
            state = State.Reloading;
        } else if (isSpeedWithinTolerance()) {
            state = State.Shooting;
        }
    }

    private boolean isSpeedWithinTolerance() {
        return shooter.getSpeed() >= targetVelocity.getAsDouble() - SPEED_TOLERANCE &&
                shooter.getSpeed() <= targetVelocity.getAsDouble() + SPEED_TOLERANCE;
    }

    private int shooterDelay = 0;

    private void shooting() {
        if (!magazine.hasFeederSensorBreak()) {
            switch (mode.getMode()) {
                case Single:
                    state = State.End;
                    break;
                case Full:
                    state = State.Reloading;
                    break;
            }
        } else if (!isSpeedWithinTolerance()) {
            state = State.Accelerating;
        } else if (shooterDelay >= maxShooterDelay) {
            if (mode.getMode() == Mode.Full) {
                magazine.startIndexMotor();
                magazine.reverseRejectMotor();
            }
            magazine.startFeederMotor();
        } else {
            shooterDelay++;
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
        magazine.stopIndexMotor();
        magazine.stopFeederMotor();
        magazine.stopRejectMotor();
    }

    @Override
    public boolean isFinished() {
        return state == State.End;
    }
}
