package frc.robot.definition;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.hardware.SaitekX52;
import org.jetbrains.annotations.NotNull;

public final class Input {
    @NotNull
    public final Joystick joystick;
    @NotNull
    public final Button collect;
    @NotNull
    public final Button deployIntake;
    @NotNull
    public final Button retrieveIntake;
    @NotNull
    public final Button shoot;
    @NotNull
    public final Button attenuate;
    @NotNull
    public final Button invert;

    // Extra
    @NotNull
    public final Button shooterMode;
    @NotNull
    public final Button shooterSpeedSlow;
    @NotNull
    public final Button shooterSpeedFast;

    @NotNull
    public final Button collectIgnoreColor;
    @NotNull
    public final Button rejectAll;
    @NotNull
    public final Button rejectIndex;


    public Input(
            @NotNull Joystick joystick,
            @NotNull Button collect,
            @NotNull Button deployIntake,
            @NotNull Button retrieveIntake,
            @NotNull Button shoot,
            @NotNull Button attenuate,
            @NotNull Button invert,

            @NotNull Button shooterMode,
            @NotNull Button shooterSpeedSlow,
            @NotNull Button shooterSpeedFast,

            @NotNull Button collectIgnoreColor,
            @NotNull Button rejectAll,
            @NotNull Button rejectIndex
    ) {
        this.joystick = joystick;
        this.collect = collect;
        this.deployIntake = deployIntake;
        this.retrieveIntake = retrieveIntake;
        this.shoot = shoot;
        this.attenuate = attenuate;
        this.invert = invert;

        this.shooterMode = shooterMode;
        this.shooterSpeedSlow = shooterSpeedSlow;
        this.shooterSpeedFast = shooterSpeedFast;

        this.collectIgnoreColor = collectIgnoreColor;
        this.rejectAll = rejectAll;
        this.rejectIndex = rejectIndex;
    }

    public Input(@NotNull Joystick joystick) {
        this(
                joystick,
                new JoystickButton(joystick, SaitekX52.Buttons.TRIGGER + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_A + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_B + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_C + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.SECOND_TRIGGER + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.PINKIE + 1),

                new JoystickButton(joystick, SaitekX52.Buttons.LAUNCH + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_1 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_2 + 1),

                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_3 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_4 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_6 + 1)
        );
    }
}
