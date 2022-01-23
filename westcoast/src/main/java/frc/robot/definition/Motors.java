package frc.robot.definition;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import org.jetbrains.annotations.NotNull;

public class Motors {
    @NotNull
    public final DriveMotors driveLeft;
    @NotNull
    public final DriveMotors driveRight;
    @NotNull
    public final MagazineMotors magazine;
    @NotNull
    public final ShooterMotors shooter;

    public Motors(
            @NotNull DriveMotors driveLeft,
            @NotNull DriveMotors driveRight,
            @NotNull MagazineMotors magazine,
            @NotNull ShooterMotors shooter
    ) {
        this.driveLeft = driveLeft;
        this.driveRight = driveRight;
        this.magazine = magazine;
        this.shooter = shooter;
    }

    public static final class DriveMotors {
        @NotNull
        public final MotorController lead;
        @NotNull
        public final MotorController follower0;
        @NotNull
        public final MotorController follower1;

        public DriveMotors(
                @NotNull MotorController lead,
                @NotNull MotorController follower0,
                @NotNull MotorController follower1
        ) {
            this.lead = lead;
            this.follower0 = follower0;
            this.follower1 = follower1;
        }
    }

    public static final class MagazineMotors {
        @NotNull
        public final MotorController intake;
        @NotNull
        public final MotorController index;
        @NotNull
        public final MotorController magazine;

        public MagazineMotors(
                @NotNull MotorController intake,
                @NotNull MotorController index,
                @NotNull MotorController magazine
        ) {
            this.intake = intake;
            this.index = index;
            this.magazine = magazine;
        }
    }

    public static final class ShooterMotors {
        @NotNull
        public final WPI_TalonSRX main;
        @NotNull
        public final WPI_TalonSRX secondary;

        public ShooterMotors(@NotNull WPI_TalonSRX main, @NotNull WPI_TalonSRX secondary) {
            this.main = main;
            this.secondary = secondary;
        }
    }

    @NotNull
    public static DriveMotors driveCTRMotors(
            @NotNull WPI_TalonSRX lead,
            @NotNull WPI_VictorSPX follower0,
            @NotNull WPI_VictorSPX follower1,
            boolean invert
    ) {
        follower0.follow(lead);
        follower0.setInverted(invert);

        follower1.follow(lead);
        follower1.setInverted(invert);

        lead.configContinuousCurrentLimit(18);
        lead.configPeakCurrentLimit(20);
        lead.configPeakCurrentDuration(45);
        lead.enableCurrentLimit(true);

        lead.setInverted(invert);
        lead.setNeutralMode(NeutralMode.Brake);

        return new DriveMotors(lead, follower0, follower1);
    }
}
