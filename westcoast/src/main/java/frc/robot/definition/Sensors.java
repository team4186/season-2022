package frc.robot.definition;

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
        public final DigitalInput head;
        @NotNull
        public final DigitalInput magazine;
        @NotNull
        public final DigitalInput tail;

        public MagazineSensors(
                @NotNull DigitalInput head,
                @NotNull DigitalInput magazine,
                @NotNull DigitalInput tail
        ) {
            this.head = head;
            this.magazine = magazine;
            this.tail = tail;
        }
    }
}
