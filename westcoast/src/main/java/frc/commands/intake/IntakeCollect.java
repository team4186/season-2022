package frc.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.IntakeSubsystem;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public final class IntakeCollect extends CommandBase {

    private enum State {
        End,
        Full,
        Collecting,
        Rejecting,
        AcceptingToFeeder,
        AcceptingToIndex,
    }

    @NotNull
    private final IntakeSubsystem intake;
    @NotNull
    private final MagazineSubsystem magazine;
    @NotNull
    private final BooleanSupplier ballAcceptanceStrategy;

    private final int rejectTickCount;
    private final int reverseIntakeTickCount;
    private final boolean finishWhenFull;

    private State state = State.Collecting;

    public IntakeCollect(
            @NotNull IntakeSubsystem intake,
            @NotNull MagazineSubsystem magazine,
            @NotNull BooleanSupplier ballAcceptanceStrategy,
            int rejectTickCount,
            int reverseIntakeTickCount,
            boolean finishWhenFull
    ) {
        this.intake = intake;
        this.magazine = magazine;
        this.ballAcceptanceStrategy = ballAcceptanceStrategy;
        this.rejectTickCount = rejectTickCount;
        this.reverseIntakeTickCount = reverseIntakeTickCount;
        this.finishWhenFull = finishWhenFull;
        addRequirements(intake, magazine);
    }

    @Override
    public void initialize() {
        state = State.Collecting;
    }

    @Override
    public void execute() {
        switch (state) {
            case Full:
                full();
                break;
            case Collecting:
                reachReject = false;
                reverseIntakeRunningTime = 0;
                collect();
                break;
            case Rejecting:
                reject();
                break;
            case AcceptingToFeeder:
                acceptToFeeder();
                break;
            case AcceptingToIndex:
                acceptToIndex();
                break;
            case End:
                break;
        }
    }

    private void full() {
        magazine.stopAll();
        intake.stop();
        if (finishWhenFull) {
            state = State.End;
        } else if (!magazine.hasIndexSensorBreak() || !magazine.hasFeederSensorBreak()) {
            state = State.Collecting;
        }
    }

    private void collect() {
        if (!magazine.hasIndexSensorBreak()) {
            intake.start();
            magazine.startIndexMotor();
        } else {
            intake.stop();
            magazine.stopIndexMotor();
            if (ballAcceptanceStrategy.getAsBoolean()) {
                if (!magazine.hasFeederSensorBreak()) {
                    state = State.AcceptingToFeeder;
                } else {
                    state = State.AcceptingToIndex;
                }
            } else {
                state = State.Rejecting;
            }
        }
    }

    private int rejectRunningTime = 0;
    private boolean reachReject = false;

    private void reject() {
        if (magazine.hasRejectSensorBreak()) {
            reachReject = true;
            rejectRunningTime = 0;
        }

        if (!reachReject) {
            magazine.startIndexMotor();
            magazine.startRejectMotor();
        } else if (rejectRunningTime < rejectTickCount) {
            rejectRunningTime++;
            magazine.startIndexMotor();
            magazine.startRejectMotor();
        } else {
            magazine.stopIndexMotor();
            magazine.stopRejectMotor();
            state = State.Collecting;
        }
    }

    private void acceptToFeeder() {
        if (!magazine.hasFeederSensorBreak()) {
            magazine.startIndexMotor();
            magazine.startFeederMotor();
            magazine.reverseRejectMotor();
        } else {
            magazine.stopIndexMotor();
            magazine.stopFeederMotor();
            magazine.stopRejectMotor();
            state = State.Collecting;
        }
    }

    private int reverseIntakeRunningTime = 0;

    private void acceptToIndex() {
        if (reverseIntakeRunningTime < reverseIntakeTickCount) {
            reverseIntakeRunningTime++;
            intake.reverse();
        } else {
            intake.stop();
            state = State.Full;
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
        magazine.stopAll();
    }

    @Override
    public boolean isFinished() {
        return state == State.End;
    }
}
