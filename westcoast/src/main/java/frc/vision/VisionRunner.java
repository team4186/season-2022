package frc.vision;

public interface VisionRunner {
    default void init() {
    }

    void periodic();

    boolean hasTarget();

    double getXOffset();

    double getAlignX();

    double getYOffset();

    double getheight();

    double getDistance();
}
