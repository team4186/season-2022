package frc.robot.definition

import frc.subsystems.*

data class Subsystems(
    val driveTrain: DriveTrainSubsystem,
    val intake: IntakeSubsystem,
    val shooter: ShooterSubsystem,
    val magazine: MagazineSubsystem,
    val climber: ClimberSubsystem
)