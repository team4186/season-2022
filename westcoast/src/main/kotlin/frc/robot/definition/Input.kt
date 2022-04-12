package frc.robot.definition

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj2.command.button.Button
import edu.wpi.first.wpilibj2.command.button.JoystickButton

class Input(
    val joystick: Joystick,
    val collect: Button,
    val deployIntake: Button,
    val retrieveIntake: Button,
    val shoot: Button,
    val attenuate: Button,
    val invert: Button,
    val turnInPlace: Button,
    val climb: Button,
    // Extra
    val shooterMode: Button,
    val shooterSpeedSlow: Button,
    val shooterSpeedFast: Button,
    val collectIgnoreColor: Button,
    val rejectAll: Button,
    val rejectIndex: Button
) {
    constructor(
        joystick: Joystick,
        collect: Int,
        deployIntake: Int,
        retrieveIntake: Int,
        shoot: Int,
        attenuate: Int,
        invert: Int,
        turnInPlace: Int,
        climb: Int,

        shooterMode: Int,
        shooterSpeedSlow: Int,
        shooterSpeedFast: Int,
        collectIgnoreColor: Int,
        rejectAll: Int,
        rejectIndex: Int
    ) : this(
        joystick = joystick,
        collect = JoystickButton(joystick, collect),
        deployIntake = JoystickButton(joystick, deployIntake),
        retrieveIntake = JoystickButton(joystick, retrieveIntake),
        shoot = JoystickButton(joystick, shoot),
        attenuate = JoystickButton(joystick, attenuate),
        invert = JoystickButton(joystick, invert),
        turnInPlace = JoystickButton(joystick, turnInPlace),
        climb = JoystickButton(joystick, climb),

        shooterMode = JoystickButton(joystick, shooterMode),
        shooterSpeedSlow = JoystickButton(joystick, shooterSpeedSlow),
        shooterSpeedFast = JoystickButton(joystick, shooterSpeedFast),
        collectIgnoreColor = JoystickButton(joystick, collectIgnoreColor),
        rejectAll = JoystickButton(joystick, rejectAll),
        rejectIndex = JoystickButton(joystick, rejectIndex)

    )
}
