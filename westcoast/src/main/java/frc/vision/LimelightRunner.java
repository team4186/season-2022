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
        SmartDashboard.putNumber("Angle Offset", getXOffset());
        SmartDashboard.putNumber("Distance", Units.metersToInches(getDistance()));
    }

    @Override
    public boolean hasTarget() {
        double tv = table.getEntry("tv").getDouble(0.0);
        return tv == 1.0;
    }

    @Override
    public double getXOffset() {
        return hasTarget() ? table.getEntry("tx").getDouble(0.0) : 0.0;
    }

    @Override
    public double getYOffset() {
        return table.getEntry("ty").getDouble(0.0);
    }

    @Override
    public double getDistance() {
        double targetHeight = 2.6416;
        double cameraHeight = 0.81; //Subject to change
        double cameraAngle = 50.0; //Subject to change (ish)
        double targetAngle = getYOffset();
        double totalAngleRad = Units.degreesToRadians(cameraAngle + targetAngle);
        double distance = (targetHeight - cameraHeight) / tan(totalAngleRad);

        return hasTarget() ? distance : Double.NaN;
    }

    @Override
    public void setLight(boolean mode) {
        double ledMode;
        ledMode = mode ? 3.0 : 1.0;
        table.getEntry("ledMode").setValue(ledMode);
    }
}