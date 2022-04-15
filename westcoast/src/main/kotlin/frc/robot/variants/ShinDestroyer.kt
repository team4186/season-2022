package frc.robot.variants

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.ColorSensorV3
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj.*
import frc.hardware.DummyGyro
import frc.hardware.SaitekX52
import frc.robot.definition.*
import frc.robot.definition.Motors.*
import frc.robot.definition.Motors.Companion.driveCTRMotors
import frc.robot.definition.Ownership.Companion.parameter
import frc.robot.definition.Sensors.DriveSensors
import frc.robot.definition.Sensors.MagazineSensors
import frc.vision.LimelightRunner

object ShinDestroyer {
    fun definition(): Definition {
        return Definition(
            name = "Shin Destroyer",
            input = Input(
                joystick = Joystick(0),
                collect = SaitekX52.Buttons.TRIGGER,
                deployIntake = SaitekX52.Buttons.FIRE_A,
                retrieveIntake = SaitekX52.Buttons.FIRE_B,
                shoot = SaitekX52.Buttons.FIRE_C,
                attenuate = SaitekX52.Buttons.SECOND_TRIGGER,
                invert = SaitekX52.Buttons.PINKIE,
                turnInPlace = SaitekX52.Buttons.FIRE_D,
                climb = SaitekX52.Buttons.LAUNCH,
                shooterMode = SaitekX52.Buttons.TOGGLE_5,
                shooterSpeedSlow = SaitekX52.Buttons.TOGGLE_1,
                shooterSpeedFast = SaitekX52.Buttons.TOGGLE_2,
                collectIgnoreColor = SaitekX52.Buttons.TOGGLE_3,
                rejectAll = SaitekX52.Buttons.TOGGLE_4,
                rejectIndex = SaitekX52.Buttons.TOGGLE_6,
            ),
            motors = Motors(
                driveLeft = driveCTRMotors(
                    lead = WPI_TalonSRX(5),
                    follower0 = WPI_VictorSPX(3),
                    follower1 = WPI_VictorSPX(4),
                    invert = true
                ),
                driveRight = driveCTRMotors(
                    lead = WPI_TalonSRX(2),
                    follower0 = WPI_VictorSPX(6),
                    follower1 = WPI_VictorSPX(7),
                    invert = false
                ),
                intake = IntakeMotors(
                    CANSparkMax(13, CANSparkMaxLowLevel.MotorType.kBrushless)
                ),
                magazine = MagazineMotors(
                    index = CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless),
                    feeder = CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless),
                    reject = CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless)
                ),
                shooter = ShooterMotors(
                    lead = CANSparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless),
                    follower = CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless)
                ),
                climber = ClimberMotors(
                    lead = CANSparkMax(14, CANSparkMaxLowLevel.MotorType.kBrushless),
                    follower = CANSparkMax(15, CANSparkMaxLowLevel.MotorType.kBrushless)
                )
            ),
            pneumatics = Pneumatics(
                DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 1)
            ),
            sensors = Sensors(
                drive = DriveSensors(
                    gyro = DummyGyro,
                    leftEncoder = encoder(8, 9),
                    rightEncoder = encoder(7, 6),
                    vision = LimelightRunner()
                ),
                magazine = MagazineSensors(
                    index = DigitalInput(2),
                    feeder = DigitalInput(1),
                    reject = DigitalInput(0),
                    colorSensor = ColorSensorV3(I2C.Port.kOnboard)
                )
            ),
            parameters = Parameters(
                perfectTurnAngleMultiplier = parameter(value = 1.04)
            ),
            controllers = Controllers(
                leaveLine = {
                    ProfiledPIDController(
                        1.0,
                        0.0,
                        0.4,
                        TrapezoidProfile.Constraints(3.0, 4.0)
                    ).apply {
                        setTolerance(0.10, 0.1)
                        disableContinuousInput()
                    }
                },

                setupShotTurn = {
                    PIDController(0.045, 0.1, 0.07).apply {
                        disableContinuousInput()
                        setTolerance(1.5)
                    }
                },

                setupShotForward = {
                    PIDController(0.55, 0.01, 0.1).apply {
                        disableContinuousInput()
                        setTolerance(0.1)
                    }
                },

                shooterConfig = {
                    p = 0.0
                    i = 0.00000033
                    iZone = 0.0
                    d = 0.0
                    ff = 0.0
                    setOutputRange(0.0, 1.0)
                },

                climber = Controllers.Climber(
                    deploy = {
                        p = 0.01
                        i = 0.0000013
                        iZone = 0.0
                        d = 0.006
                        ff = 0.0
                        setOutputRange(0.0, 1.0)
                    },

                    climb = {
                        p = 0.06
                        i = 0.0000005
                        iZone = 0.0
                        d = 0.0
                        ff = 0.0
                        setOutputRange(0.0, 1.0)
                    }
                )
            )
        )
    }

    private fun encoder(channelA: Int, channelB: Int): Encoder {
        val encoder = Encoder(channelA, channelB)
        encoder.distancePerPulse = 0.0018868 // 530 pulses = 1 m
        return encoder
    }
}
