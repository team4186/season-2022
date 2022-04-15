package frc.robot.definition

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.interfaces.Gyro
import frc.vision.VisionRunner

data class Sensors(
    val drive: DriveSensors,
    val magazine: MagazineSensors
) {
    data class DriveSensors(
        val gyro: Gyro,
        val leftEncoder: Encoder,
        val rightEncoder: Encoder,
        val vision: VisionRunner
    )

    data class MagazineSensors(
        val index: DigitalInput,
        val feeder: DigitalInput,
        val reject: DigitalInput,
        val colorSensor: ColorSensorV3
    )
}