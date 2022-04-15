package frc.robot.definition

import frc.subsystems.*

data class Definition(
    val name: String,
    val input: Input,
    val motors: Motors,
    val pneumatics: Pneumatics,
    val sensors: Sensors,
    val parameters: Parameters,
    val controllers: Controllers,
    val subsystems: Subsystems = Subsystems(
        driveTrain = DriveTrainSubsystem(
            leftMotor = motors.driveLeft.lead,
            rightMotor = motors.driveRight.lead,
            leftEncoder = sensors.drive.leftEncoder,
            rightEncoder = sensors.drive.rightEncoder,
            gyro = sensors.drive.gyro,
            vision = sensors.drive.vision
        ),
        intake = IntakeSubsystem(
            deploy = pneumatics.intakeDeploy,
            intakeMotor = motors.intake.main
        ),
        shooter = ShooterSubsystem(
            shooterMotor = motors.shooter.lead,
            configurator = { controllers.shooterConfig(this) }
        ),
        magazine = MagazineSubsystem(
            indexMotor = motors.magazine.index,
            feederMotor = motors.magazine.feeder,
            rejectMotor = motors.magazine.reject,
            indexSensor = sensors.magazine.index,
            feederSensor = sensors.magazine.feeder,
            rejectSensor = sensors.magazine.reject,
            colorSensor = sensors.magazine.colorSensor
        ),
        climber = ClimberSubsystem(
            climberMotor = motors.climber.lead,
            climberConfigDeploy = { controllers.climber.deploy(this) },
            climberConfigClimb = { controllers.climber.climb(this) }
        )
    )
)