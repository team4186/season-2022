package frc.robot.definition

import frc.commands.drive.PerfectTurn
import frc.robot.definition.Ownership.DoubleParameter

class Parameters(
    val perfectTurnAngleMultiplier: DoubleParameter<PerfectTurn>
)