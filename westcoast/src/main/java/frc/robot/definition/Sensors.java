package frc.robot.definition;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.vision.VisionRunner;
import org.jetbrains.annotations.NotNull;

public class Sensors {
    @NotNull
    public final DriveSensors drive;
    @NotNull
    public final MagazineSensors magazine;

    public Sensors(@NotNull DriveSensors drive, @NotNull MagazineSensors magazine) {
        this.drive = drive;
        this.magazine = magazine;
    }

    public static class DriveSensors {
        @NotNull
        public final Gyro gyro;
        @NotNull
        public final Encoder leftEncoder;
        @NotNull
        public final Encoder rightEncoder;
        @NotNull
        public final VisionRunner vision;

        public DriveSensors(
                @NotNull Gyro gyro,
                @NotNull Encoder leftEncoder,
                @NotNull Encoder rightEncoder,
                @NotNull VisionRunner vision
        ) {
            this.gyro = gyro;
            this.leftEncoder = leftEncoder;
            this.rightEncoder = rightEncoder;
            this.vision = vision;
        }
    }

    public static final class MagazineSensors {
        @NotNull
        public final DigitalInput index;
        @NotNull
        public final DigitalInput feeder;
        @NotNull
        public final DigitalInput reject;
        @NotNull
        public final ColorSensorV3 colorSensor;

        public MagazineSensors(
                @NotNull DigitalInput index,
                @NotNull DigitalInput feeder,
                @NotNull DigitalInput reject,
                @NotNull ColorSensorV3 colorSensor
        ) {
            this.index = index;
            this.feeder = feeder;
            this.reject = reject;
            this.colorSensor = colorSensor;
        }
    }
}
