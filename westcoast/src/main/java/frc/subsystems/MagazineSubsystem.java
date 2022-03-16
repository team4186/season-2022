package frc.subsystems;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;


public class MagazineSubsystem extends SubsystemBase {

    public static final Color BlueTarget = new Color(0.143, 0.427, 0.429);
    public static final Color RedTarget = new Color(0.561, 0.232, 0.114);
    @NotNull
    private static final ColorMatch colorMatcher = new ColorMatch();

    static {
        colorMatcher.addColorMatch(BlueTarget);
        colorMatcher.addColorMatch(RedTarget);
    }

    @NotNull
    private final MotorController indexMotor;
    @NotNull
    private final MotorController feederMotor;
    @NotNull
    private final MotorController rejectMotor;
    @NotNull
    private final DigitalInput indexSensor;
    @NotNull
    private final DigitalInput feederSensor;
    @NotNull
    private final ColorSensorV3 colorSensor;

    public MagazineSubsystem(
            @NotNull MotorController indexMotor,
            @NotNull MotorController feederMotor,
            @NotNull MotorController rejectMotor,
            @NotNull DigitalInput indexSensor,
            @NotNull DigitalInput feederSensor,
            @NotNull ColorSensorV3 colorSensor
    ) {
        this.indexMotor = indexMotor;
        this.feederMotor = feederMotor;
        this.rejectMotor = rejectMotor;
        this.indexSensor = indexSensor;
        this.feederSensor = feederSensor;
        this.colorSensor = colorSensor;
    }

    public boolean hasIndexSensorBreak() {
        return indexSensor.get();
    }

    public boolean hasFeederSensorBreak() {
        return feederSensor.get();
    }

    public boolean isMatchingColor(Color color) {
        Color pickedColor = colorSensor.getColor();
        SmartDashboard.putString("Intake Color", String.format(
                "Color(%2f, %2f, %2f)",
                pickedColor.red,
                pickedColor.green,
                pickedColor.blue
        ));
        return colorMatcher
                .matchClosestColor(pickedColor)
                .color == color;
    }

    public void startIndexMotor() {
        indexMotor.set(0.5);
    }

    public void stopIndexMotor() {
        indexMotor.stopMotor();
    }

    public void startFeederMotor() {
        feederMotor.set(0.5);
    }

    public void reverseFeederMotor() {
        feederMotor.set(-0.5);
    }

    public void stopFeederMotor() {
        feederMotor.stopMotor();
    }

    public void startRejectMotor() {
        rejectMotor.set(0.5);
    }

    public void stopRejectMotor() {
        rejectMotor.stopMotor();
    }

    public void stopAll() {
        feederMotor.stopMotor();
        indexMotor.stopMotor();
        rejectMotor.stopMotor();
    }
}
