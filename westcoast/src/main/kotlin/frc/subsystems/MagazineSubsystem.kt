package frc.subsystems

import com.revrobotics.ColorMatch
import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.motorcontrol.MotorController
import edu.wpi.first.wpilibj.util.Color
import edu.wpi.first.wpilibj2.command.SubsystemBase

class MagazineSubsystem(
    private val indexMotor: MotorController,
    private val feederMotor: MotorController,
    private val rejectMotor: MotorController,
    private val indexSensor: DigitalInput,
    private val feederSensor: DigitalInput,
    private val rejectSensor: DigitalInput,
    private val colorSensor: ColorSensorV3
) : SubsystemBase() {
    fun hasIndexSensorBreak(): Boolean {
        return !indexSensor.get()
    }

    fun hasFeederSensorBreak(): Boolean {
        return !feederSensor.get()
    }

    fun hasRejectSensorBreak(): Boolean {
        return !rejectSensor.get()
    }

    fun isMatchingColor(color: Target): Boolean {
        val pickedColor = colorSensor.color
        //        SmartDashboard.putString("Intake Color", String.format(
//                "Color(%2f, %2f, %2f)",
//                pickedColor.red,
//                pickedColor.green,
//                pickedColor.blue
//        ));
        return colorMatcher.matchClosestColor(pickedColor).color == color
    }

    fun startIndexMotor() {
        indexMotor.set(0.8)
    }

    fun stopIndexMotor() {
        indexMotor.stopMotor()
    }

    fun startFeederMotor() {
        feederMotor.set(-0.5)
    }

    fun reverseFeederMotor() {
        feederMotor.set(0.8)
    }

    fun stopFeederMotor() {
        feederMotor.stopMotor()
    }

    fun startRejectMotor() {
        rejectMotor.set(0.8)
    }

    fun reverseRejectMotor() {
        rejectMotor.set(-0.8)
    }

    fun stopRejectMotor() {
        rejectMotor.stopMotor()
    }

    fun stopAll() {
        feederMotor.stopMotor()
        indexMotor.stopMotor()
        rejectMotor.stopMotor()
    }

    sealed interface Target {
        object Blue : Color(0.143, 0.427, 0.429), Target
        object Red : Color(0.406, 0.406, 0.188), Target
    }

    companion object {
        private val colorMatcher = ColorMatch().apply {
            addColorMatch(Target.Blue)
            addColorMatch(Target.Red)
        }
    }
}