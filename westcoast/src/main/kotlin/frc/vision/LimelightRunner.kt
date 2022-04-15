package frc.vision

import edu.wpi.first.math.util.Units
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlin.math.tan

class LimelightRunner(
    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")
) : VisionRunner {

    override fun periodic() {
        SmartDashboard.putBoolean("Has Target?", hasTarget)
        SmartDashboard.putNumber("Angle Offset", xOffset)
        SmartDashboard.putNumber("Distance", Units.metersToInches(distance))
    }

    override val hasTarget: Boolean
        get() {
            val tv = if (yOffset >= 0.0) table.getEntry("tv").getDouble(0.0) else 0.0
            return tv == 1.0
        }

    override val xOffset: Double get() = if (hasTarget) table.getEntry("tx").getDouble(0.0) else 0.0
    override val yOffset: Double get() = table.getEntry("ty").getDouble(0.0)

    //Subject to change
    override val distance: Double
        get() {
            val targetHeight = 2.6416
            val cameraHeight = 0.81 //Subject to change

            val cameraAngle = 50.0 //Subject to change (ish)

            val targetAngle: Double = yOffset
            val totalAngleRad = Units.degreesToRadians(cameraAngle + targetAngle)
            val distance = (targetHeight - cameraHeight) / tan(totalAngleRad)

            return if (hasTarget) distance else Double.NaN
        }


    override fun setLight(mode: Boolean) {
        table.getEntry("ledMode").setValue(if (mode) 3.0 else 1.0)
    }
}