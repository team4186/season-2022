package frc.commands.intake;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.IntakeSubsystem;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public final class IntakeCollect extends CommandBase {

    public interface ColorSupplier {
        Color getColor();
    }

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
    private final ColorSupplier color;

    private final int rejectTickCount;
    private final int reverseIntakeTickCount;
    private final boolean finishWhenFull;

    private State state = State.Collecting;

    public IntakeCollect(
            @NotNull IntakeSubsystem intake,
            @NotNull MagazineSubsystem magazine,
            @NotNull ColorSupplier color,
            int rejectTickCount,
            int reverseIntakeTickCount,
            boolean finishWhenFull
    ) {
        this.intake = intake;
        this.magazine = magazine;
        this.color = color;
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
        SmartDashboard.putString("State", state.toString());
        switch (state) {
            case Full:
                full();
                break;
            case Collecting:
                rejectRunningTime = 0;
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
            if (magazine.isMatchingColor(color.getColor())) {
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

    private void reject() {
        if (magazine.hasRejectSensorBreak() || rejectRunningTime < rejectTickCount) {
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
