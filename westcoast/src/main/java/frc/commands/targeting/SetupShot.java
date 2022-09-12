package frc.commands.targeting;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.definition.Definition;
import frc.subsystems.DriveTrainSubsystem;
import org.jetbrains.annotations.NotNull;

public final class SetupShot extends CommandBase {
    @NotNull
    private final PIDController turn;
    @NotNull
    private final PIDController forward;
    @NotNull
    private final DriveTrainSubsystem drive;
    private final double distance;
    private int turnOnTarget;
    private int forwardOnTarget;
    private int targetLost;
    private boolean hasInitialTarget;

    public SetupShot(
            @NotNull Definition definition,
            double distance
    ) {
        this.turn = definition.controllers.alignToTarget();
        this.forward = definition.controllers.forwardAlignToTarget();
        this.drive = definition.subsystems.driveTrain;
        this.distance = distance;
    }

    @Override
    public void initialize() {
        turn.reset();
        forward.reset();
        turnOnTarget = 0;
        forwardOnTarget = 0;
        targetLost = 0;
        hasInitialTarget = true;//drive.vision.hasTarget();
    }

    @Override
    public void execute() {
        double forwardValue = forward.calculate(drive.vision.getDistance(), distance);
        double turnValue = -turn.calculate(drive.vision.getXOffset(), 0.0);
        drive.arcade(
                forwardValue,
                turnValue,
                false
        );
        turnOnTarget = turn.atSetpoint() ? (turnOnTarget + 1) : 0;
        forwardOnTarget = forward.atSetpoint() ? (forwardOnTarget + 1) : 0;
        targetLost = !drive.vision.hasTarget() ? (targetLost + 1) : 0;
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return ((turnOnTarget > 10) && (forwardOnTarget > 10)) || (!hasInitialTarget) || (targetLost > 5);
    }
}