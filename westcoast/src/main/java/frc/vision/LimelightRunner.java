package frc.vision;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static java.lang.Math.tan;

public class LimelightRunner implements VisionRunner {
    private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Has Target?", hasTarget());
        SmartDashboard.putNumber("Alignment Offset", getAlignX());
        SmartDashboard.putNumber("Distance", getDistance());
    }

    @Override
    public boolean hasTarget() {
        double tv = getYOffset() >= 0.0 ? table.getEntry("tv").getDouble(0.0) : 0.0;
        return tv == 1.0;
    }

    @Override
    public double getXOffset() {
        return hasTarget() ? table.getEntry("tx").getDouble(0.0) : 0.0;
    }

    @Override
    public double getAlignX() {
        return scaler(getXOffset());
    }

    @Override
    public double getYOffset() {
        return table.getEntry("ty").getDouble(0.0);
    }

    @Override
    public double getheight() {

        return table.getEntry("tvert").getDouble(0.0);
    }

    @Override
    public double getDistance() {
        double targetHeight = 98.125;
        double cameraHeight = 14.0; //Subject to change
        double cameraAngle = 12.632155; //Subject to change (ish)
        double targetAngle = getYOffset();
        double totalAngleRad = Units.degreesToRadians(cameraAngle + targetAngle);
        double distance = (targetHeight - cameraHeight) / tan(totalAngleRad);

        return hasTarget() ? distance / 12 : Double.NaN;
    }

    private double scaler(double value) {
        return value * 0.0335570469798658;
    }
}