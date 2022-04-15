package frc.commands.drive

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.subsystems.DriveTrainSubsystem
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sin

class CheesyDrive(
    private val inputThrottle: () -> Double,
    private val inputYaw: () -> Double,
    private val isHighGear: () -> Boolean,
    private val isQuickTurn: () -> Boolean,
    private val forward: () -> Double,
    private val drive: DriveTrainSubsystem,
    private val sensitivityHigh: Double,
    private val sensitivityLow: Double
) : CommandBase() {
    private var oldWheel = 0.0
    private var quickStopAccumulator = 0.0
    override fun execute() {
        val wheelNonLinearity: Double
        // NOTE do we need a deadband?
//        double wheel = handleDeadband(getWheel(stick), C.Drive.wheelDeadband);
        var wheel = -inputYaw()

        // NOTE do we need a deadband?
//        double throttle = -handleDeadband(getThrottle(stick), C.Drive.throttleDeadband);
        val throttle = inputThrottle() * forward()
        val negInertia = wheel - oldWheel

        /*
             * if(getAverageSpeed()> 2000){ SetHighGear(); } else if (getAverageSpeed() <
             * 1500){ SetLowGear(); } //Autoshifting code based on a speed threshold from encoder data (not implemented)
             */

        oldWheel = wheel
        // Apply a sin function that's scaled to make it feel better. WPILib does similar thing by squaring inputs.
        if (isHighGear()) {
            wheelNonLinearity = 0.6
        } else {
            wheelNonLinearity = 0.5
            wheel = sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / sin(Math.PI / 2.0 * wheelNonLinearity)
        }
        wheel = sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / sin(Math.PI / 2.0 * wheelNonLinearity)
        wheel = sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / sin(Math.PI / 2.0 * wheelNonLinearity)
        var leftPwm: Double
        var rightPwm: Double
        val overPower: Double
        val sensitivity: Double
        val angularPower: Double
        // Negative inertia!
        var negInertiaAccumulator = 0.0
        val negInertiaScalar: Double

        if (isHighGear()) {
            negInertiaScalar = 5.0
            sensitivity = sensitivityHigh
        } else {
            negInertiaScalar = when {
                wheel * negInertia > 0 -> 2.5
                wheel.absoluteValue > 0.65 -> 5.0
                else -> 3.0
            }

            sensitivity = sensitivityLow
            // useless code?
//            if (Math.abs(throttle) > 0.1) {
            // sensitivity = 1.0 - (1.0 - sensitivity) / Math.abs(throttle);
//            }
        }

        val negInertiaPower = negInertia * negInertiaScalar
        negInertiaAccumulator += negInertiaPower
        wheel += negInertiaAccumulator
        // NOTE useless code?
//        if (negInertiaAccumulator > 1) {
//            negInertiaAccumulator -= 1;
//        } else if (negInertiaAccumulator < -1) {
//            negInertiaAccumulator += 1;
//        } else {
//            negInertiaAccumulator = 0;
//        }
        val linearPower: Double = throttle
        // Quickturn!
        if (isQuickTurn()) {
            if (abs(linearPower) < 0.2) {
                val alpha = 0.1
                quickStopAccumulator = (1.0 - alpha) * quickStopAccumulator + alpha * wheel.coerceIn(-1.0, 1.0) * 5
            }
            overPower = 1.0
            // NOTE useless code?
//            if (isHighGear.getAsBoolean()) {
//                sensitivity = .005;
//            } else {
//                sensitivity = 0.005;
//
//            }
            angularPower = wheel
        } else {
            overPower = 0.0
            angularPower = throttle.absoluteValue * wheel * sensitivity - quickStopAccumulator
            quickStopAccumulator = when {
                quickStopAccumulator > 1 -> quickStopAccumulator - 1.0
                quickStopAccumulator < -1 -> quickStopAccumulator + 1.0
                else -> 0.0
            }

        }
        leftPwm = linearPower
        rightPwm = leftPwm
        leftPwm -= angularPower
        rightPwm += angularPower
        when {
            leftPwm > 1.0 -> {
                rightPwm -= overPower * (leftPwm - 1.0)
                leftPwm = 1.0
            }
            rightPwm > 1.0 -> {
                leftPwm -= overPower * (rightPwm - 1.0)
                rightPwm = 1.0
            }
            leftPwm < -1.0 -> {
                rightPwm += overPower * (-1.0 - leftPwm)
                leftPwm = -1.0
            }
            rightPwm < -1.0 -> {
                leftPwm += overPower * (-1.0 - rightPwm)
                rightPwm = -1.0
            }
        }
        drive.setMotorOutput(leftPwm, rightPwm)
    }
}