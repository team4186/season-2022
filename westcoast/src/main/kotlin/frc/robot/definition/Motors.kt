package frc.robot.definition

import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.motorcontrol.MotorController

data class Motors(
    val driveLeft: DriveMotors,
    val driveRight: DriveMotors,
    val intake: IntakeMotors,
    val magazine: MagazineMotors,
    val shooter: ShooterMotors,
    val climber: ClimberMotors
) {
    class DriveMotors(
        val lead: MotorController,
        val follower0: MotorController,
        val follower1: MotorController
    )

    class IntakeMotors(val main: MotorController)
    class MagazineMotors(
        val index: MotorController,
        val feeder: MotorController,
        val reject: MotorController
    )

    class ShooterMotors(
        val lead: CANSparkMax,
        follower: CANSparkMax
    ) {
        init {
            lead.idleMode = CANSparkMax.IdleMode.kCoast
            follower.follow(lead, true)
            follower.idleMode = CANSparkMax.IdleMode.kCoast
        }
    }

    class ClimberMotors(
        val lead: CANSparkMax,
        follower: CANSparkMax
    ) {
        init {
            lead.idleMode = CANSparkMax.IdleMode.kBrake
            follower.follow(lead, true)
            follower.idleMode = CANSparkMax.IdleMode.kBrake
        }
    }

    companion object {
        fun driveCTRMotors(
            lead: WPI_TalonSRX,
            follower0: WPI_VictorSPX,
            follower1: WPI_VictorSPX,
            invert: Boolean
        ): DriveMotors {
            // region Follower
            // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#follower
            follower0.follow(lead)
            follower0.setInverted(InvertType.FollowMaster)
            follower1.follow(lead)
            follower1.setInverted(InvertType.FollowMaster)

            // endregion

            // region Lead
            // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#current-limit
            lead.configSupplyCurrentLimit(
                SupplyCurrentLimitConfiguration(
                    true,
                    10.0,
                    20.0,
                    0.02
                )
            )
            lead.inverted = invert

            // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#neutral-mode
            lead.setNeutralMode(NeutralMode.Brake)

            // endregion

            // region Voltage Saturation
            // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#voltage-compensation
            lead.configVoltageCompSaturation(11.0)
            lead.enableVoltageCompensation(true)
            follower0.configVoltageCompSaturation(11.0)
            follower0.enableVoltageCompensation(true)
            follower1.configVoltageCompSaturation(11.0)
            follower1.enableVoltageCompensation(true)

            // endregion
            return DriveMotors(lead, follower0, follower1)
        }
    }
}