package frc.commands.intake;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.IntakeSubsystem;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public final class IntakeCollect extends CommandBase {

    private static final int STATE_END = -1;
    private static final int STATE_FULL = 0;
    private static final int STATE_COLLECT = 1;
    private static final int STATE_REJECT = 2;
    private static final int STATE_ACCEPT_TO_FEEDER = 3;
    private static final int STATE_ACCEPT_TO_INDEX = 4;

    @NotNull
    private final IntakeSubsystem intake;
    @NotNull
    private final MagazineSubsystem magazine;
    @NotNull
    private final Color color;

    private final int rejectTickCount;
    private final int reverseIntakeTickCount;
    private final boolean finishWhenFull;

    private int state = STATE_COLLECT;

    public IntakeCollect(
            @NotNull IntakeSubsystem intake,
            @NotNull MagazineSubsystem magazine,
            @NotNull Color color,
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
        state = STATE_COLLECT;
    }

    @Override
    public void execute() {
        switch (state) {
            case STATE_FULL:
                full();
                break;
            case STATE_COLLECT:
                rejectRunningTime = 0;
                reverseIntakeRunningTime = 0;
                collect();
                break;
            case STATE_REJECT:
                reject();
                break;
            case STATE_ACCEPT_TO_FEEDER:
                acceptToFeeder();
                break;
            case STATE_ACCEPT_TO_INDEX:
                acceptToIndex();
                break;
        }
    }

    private void full() {
        magazine.stopAll();
        intake.stop();
        if (finishWhenFull) {
            state = STATE_END;
        } else if (!magazine.hasIndexSensorBreak() || !magazine.hasFeederSensorBreak()) {
            state = STATE_COLLECT;
        }
    }

    private void collect() {
        if (!magazine.hasIndexSensorBreak()) {
            intake.start();
            magazine.startIndexMotor();
        } else {
            intake.stop();
            magazine.stopIndexMotor();
            if (magazine.isMatchingColor(color)) {
                if (!magazine.hasFeederSensorBreak()) {
                    state = STATE_ACCEPT_TO_FEEDER;
                } else {
                    state = STATE_ACCEPT_TO_INDEX;
                }
            } else {
                state = STATE_REJECT;
            }
        }
    }

    private int rejectRunningTime = 0;

    private void reject() {
        if (rejectRunningTime < rejectTickCount) {
            rejectRunningTime++;
            magazine.startIndexMotor();
            magazine.startRejectMotor();
        } else {
            magazine.stopIndexMotor();
            magazine.stopRejectMotor();
            state = STATE_COLLECT;
        }
    }

    private void acceptToFeeder() {
        if (!magazine.hasFeederSensorBreak()) {
            magazine.startIndexMotor();
            magazine.startFeederMotor();
        } else {
            magazine.stopIndexMotor();
            magazine.stopFeederMotor();
            state = STATE_COLLECT;
        }
    }

    private int reverseIntakeRunningTime = 0;

    private void acceptToIndex() {
        if (reverseIntakeRunningTime < reverseIntakeTickCount) {
            reverseIntakeRunningTime++;
            intake.reverse();
        } else {
            intake.stop();
            state = STATE_FULL;
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
        magazine.stopAll();
    }

    @Override
    public boolean isFinished() {
        return state == STATE_END;
    }
}
