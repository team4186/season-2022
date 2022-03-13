package frc.robot.definition;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import org.jetbrains.annotations.NotNull;

public class Motors {
    @NotNull
    public final DriveMotors driveLeft;
    @NotNull
    public final DriveMotors driveRight;
    @NotNull
    public final IntakeMotors intake;
    @NotNull
    public final MagazineMotors magazine;
    @NotNull
    public final ShooterMotors shooter;

    public Motors(
            @NotNull DriveMotors driveLeft,
            @NotNull DriveMotors driveRight,
            @NotNull IntakeMotors intake, @NotNull MagazineMotors magazine,
            @NotNull ShooterMotors shooter
    ) {
        this.driveLeft = driveLeft;
        this.driveRight = driveRight;
        this.intake = intake;
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

    public static final class IntakeMotors {
        @NotNull
        public final MotorController main;

        public IntakeMotors(@NotNull MotorController main) {
            this.main = main;
        }
    }

    public static final class MagazineMotors {
        @NotNull
        public final MotorController index;
        @NotNull
        public final MotorController magazine;

        public MagazineMotors(
                @NotNull MotorController index,
                @NotNull MotorController magazine
        ) {
            this.index = index;
            this.magazine = magazine;
        }
    }

    public static final class ShooterMotors {
        @NotNull
        public final CANSparkMax main;
        @NotNull
        public final CANSparkMax secondary;

        public ShooterMotors(
                @NotNull CANSparkMax main,
                @NotNull CANSparkMax secondary
        ) {
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
        // region Follower
        // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#follower

        follower0.follow(lead);
        follower0.setInverted(InvertType.FollowMaster);

        follower1.follow(lead);
        follower1.setInverted(InvertType.FollowMaster);

        // endregion

        // region Lead
        // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#current-limit

        lead.configSupplyCurrentLimit(
                new SupplyCurrentLimitConfiguration(
                        true,
                        10,
                        20,
                        0.02
                )
        );

        lead.setInverted(invert);

        // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#neutral-mode
        lead.setNeutralMode(NeutralMode.Brake);

        // endregion

        // region Voltage Saturation
        // See https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#voltage-compensation

        lead.configVoltageCompSaturation(11);
        lead.enableVoltageCompensation(true);

        follower0.configVoltageCompSaturation(11);
        follower0.enableVoltageCompensation(true);

        follower1.configVoltageCompSaturation(11);
        follower1.enableVoltageCompensation(true);

        // endregion

        return new DriveMotors(lead, follower0, follower1);
    }
}
