package frc.vision;

public interface VisionRunner {
    default void init() {
    }

    void periodic();

    boolean hasTarget();

    double getXOffset();

    double getYOffset();

    double getDistance();

    void setLight(boolean mode);
}
