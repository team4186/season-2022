package frc.commands.intake;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystems.IntakeSubsystem;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public class IntakeCollect extends CommandBase {

    private static final int STATE_WAIT = 0;
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

    private int state = STATE_WAIT;

    public IntakeCollect(
            @NotNull IntakeSubsystem intake,
            @NotNull MagazineSubsystem magazine,
            @NotNull Color color
    ) {
        this.intake = intake;
        this.magazine = magazine;
        this.color = color;
        addRequirements(intake, magazine);
    }

    @Override
    public void initialize() {
        if (magazine.hasFeederSensorBreak() && magazine.hasIndexSensorBreak()) {
            state = STATE_WAIT;
        } else {
            state = STATE_COLLECT;
        }
    }

    @Override
    public void execute() {
        switch (state) {
            case STATE_WAIT:
                stateWait();
                break;
            case STATE_COLLECT:
                collect();
                break;
            case STATE_REJECT:
                rejectRunningTime = 0;
                reject();
                break;
            case STATE_ACCEPT_TO_FEEDER:
                acceptToFeeder();
                break;
            case STATE_ACCEPT_TO_INDEX:
                reverseIntakeRunningTime = 0;
                acceptToIndex();
                break;
        }
    }

    private void stateWait() {
        magazine.stopAll();
        intake.stop();
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
        if (rejectRunningTime < 50) {
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
            magazine.startIndexMotor();
            magazine.startFeederMotor();
            state = STATE_COLLECT;
        }
    }

    private int reverseIntakeRunningTime = 0;

    private void acceptToIndex() {
        if (reverseIntakeRunningTime < 50) {
            reverseIntakeRunningTime++;
            intake.reverse();
        } else {
            intake.stop();
            state = STATE_WAIT;
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
        magazine.stopFeederMotor();
    }
}
