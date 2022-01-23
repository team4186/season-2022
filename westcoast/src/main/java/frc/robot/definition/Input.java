package frc.robot.definition;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.hardware.SaitekX52;
import org.jetbrains.annotations.NotNull;

public final class Input {
    public final double forward;
    @NotNull
    public final Joystick joystick;
    @NotNull
    public final Button intake;
    @NotNull
    public final Button cancelIntake;
    @NotNull
    public final Button reverseIntake;
    @NotNull
    public final Button shoot;
    @NotNull
    public final Button ejectAll;
    @NotNull
    public final Button attenuate;

    public Input(
            double forward,
            @NotNull Joystick joystick,
            @NotNull Button intake,
            @NotNull Button cancelIntake,
            @NotNull Button reverseIntake,
            @NotNull Button shoot,
            @NotNull Button ejectAll,
            @NotNull Button attenuate
    ) {
        this.forward = forward;
        this.joystick = joystick;
        this.intake = intake;
        this.cancelIntake = cancelIntake;
        this.reverseIntake = reverseIntake;
        this.shoot = shoot;
        this.ejectAll = ejectAll;
        this.attenuate = attenuate;
    }

    public Input(double forward, @NotNull Joystick joystick) {
        this(
                forward,
                joystick,
                new JoystickButton(joystick, SaitekX52.Buttons.TRIGGER + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.PINKIE + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_A + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_C + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_D + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_C + 1)
        );
    }
}
