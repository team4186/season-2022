package frc.hardware

import edu.wpi.first.wpilibj.interfaces.Gyro

object DummyGyro : Gyro {
    override fun calibrate() {}
    override fun reset() {}
    override fun getAngle(): Double {
        return 0.0
    }

    override fun getRate(): Double {
        return 0.0
    }

    override fun close() {}
}
