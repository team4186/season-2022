package frc.robot.definition;

import frc.commands.drive.LeaveLine;
import frc.commands.drive.PerfectTurn;
import frc.robot.definition.Ownership.DoubleParameter;
import org.jetbrains.annotations.NotNull;

public final class Parameters {
    @NotNull public final DoubleParameter<LeaveLine> leaveLineDistanceMultiplier;
    @NotNull public final DoubleParameter<PerfectTurn> perfectTurnAngleMultiplier;

    public Parameters(
            @NotNull DoubleParameter<LeaveLine> leaveLineDistanceMultiplier,
            @NotNull DoubleParameter<PerfectTurn> perfectTurnAngleMultiplier
    ) {
        this.leaveLineDistanceMultiplier = leaveLineDistanceMultiplier;
        this.perfectTurnAngleMultiplier = perfectTurnAngleMultiplier;
    }
}
