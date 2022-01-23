package frc.robot.definition;

import frc.subsystems.DriveTrainSubsystem;
import frc.subsystems.MagazineSubsystem;
import org.jetbrains.annotations.NotNull;

public class Definition {
    @NotNull
    public final String name;
    @NotNull
    public final Input input;
    @NotNull
    public final Motors motors;
    @NotNull
    public final Sensors sensors;
    @NotNull
    public final Subsystems subsystems;
    @NotNull
    public final Parameters parameters;
    @NotNull
    public final Controllers controllers;

    public Definition(
            @NotNull String name,
            @NotNull Input input,
            @NotNull Motors motors,
            @NotNull Sensors sensors,
            @NotNull Subsystems subsystems,
            @NotNull Parameters parameters,
            @NotNull Controllers controllers
    ) {
        this.name = name;
        this.input = input;
        this.motors = motors;
        this.sensors = sensors;
        this.subsystems = subsystems;
        this.parameters = parameters;
        this.controllers = controllers;
    }

    public static Definition definition(
            @NotNull String name,
            @NotNull Input input,
            @NotNull Motors motors,
            @NotNull Sensors sensors,
            @NotNull Parameters parameters,
            @NotNull Controllers controllers
    ) {
        return new Definition(
                name,
                input,
                motors,
                sensors,
                subsystems(motors, sensors),
                parameters,
                controllers
        );
    }

    @NotNull
    private static Subsystems subsystems(@NotNull Motors motors, @NotNull Sensors sensors) {
        return new Subsystems(
                new DriveTrainSubsystem(
                        motors.driveLeft.lead,
                        motors.driveRight.lead,
                        sensors.drive.leftEncoder,
                        sensors.drive.rightEncoder,
                        sensors.drive.gyro,
                        sensors.drive.vision
                ),
                new MagazineSubsystem(
                        motors.magazine.intake,
                        motors.magazine.index,
                        motors.magazine.magazine,
                        motors.shooter.main,
                        motors.shooter.secondary,
                        sensors.magazine.head,
                        sensors.magazine.magazine,
                        sensors.magazine.tail
                )
        );
    }

}

