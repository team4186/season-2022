package frc.vision;

public interface VisionRunner {
    default void init() {
    }

    void periodic();

    boolean hasTarget();

    double getXOffset();

    double getYOffset();

    double getheight();

    double getDistance();

    void setLight(boolean mode);
}
