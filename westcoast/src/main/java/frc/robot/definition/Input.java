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
    @NotNull
    public final Button turnInPlace;

    // Debug buttons
    @NotNull
    public final Button runIntakeMotor;
    @NotNull
    public final Button runIndexMotor;
    @NotNull
    public final Button runFeederMotor;
    @NotNull
    public final Button runRejectMotor;
    @NotNull
    public final Button runShooterMotor;


    public Input(
            @NotNull Joystick joystick,
            @NotNull Button collect,
            @NotNull Button deployIntake,
            @NotNull Button retrieveIntake,
            @NotNull Button shoot,
            @NotNull Button attenuate,
            @NotNull Button invert,
            @NotNull Button turnInPlace,
            @NotNull Button runIntakeMotor,
            @NotNull Button runIndexMotor,
            @NotNull Button runFeederMotor,
            @NotNull Button runRejectMotor,
            @NotNull Button runShooterMotor
    ) {
        this.joystick = joystick;
        this.collect = collect;
        this.deployIntake = deployIntake;
        this.retrieveIntake = retrieveIntake;
        this.shoot = shoot;
        this.attenuate = attenuate;
        this.invert = invert;
        this.turnInPlace = turnInPlace;

        this.runIntakeMotor = runIntakeMotor;
        this.runIndexMotor = runIndexMotor;
        this.runFeederMotor = runFeederMotor;
        this.runRejectMotor = runRejectMotor;
        this.runShooterMotor = runShooterMotor;
    }

    public Input(@NotNull Joystick joystick) {
        this(
                joystick,
                new JoystickButton(joystick, SaitekX52.Buttons.TRIGGER + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_A + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_B + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_C + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.PINKIE + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_1 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.FIRE_D + 1),

                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_2 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_3 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_4 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_5 + 1),
                new JoystickButton(joystick, SaitekX52.Buttons.TOGGLE_6 + 1)
        );
    }
}
