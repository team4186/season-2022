package frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.NotNull;


public class MagazineSubsystem extends SubsystemBase {
    @NotNull
    private final MotorController intakeMotor;
    @NotNull
    private final MotorController indexMotor;
    @NotNull
    private final MotorController magazineMotor;
    @NotNull
    private final MotorController leftShooter;
    @NotNull
    private final MotorController rightShooter;
    @NotNull
    private final DigitalInput headSensor;
    @NotNull
    private final DigitalInput abSensor;
    @NotNull
    private final DigitalInput tailSensor;

    private double amps = 0.0;
    private double percent = 0.0;
    private double indexCount = 0.0;

    public MagazineSubsystem(
            @NotNull MotorController intakeMotor,
            @NotNull MotorController indexMotor,
            @NotNull MotorController magazineMotor,
            @NotNull MotorController leftShooter,
            @NotNull MotorController rightShooter,
            @NotNull DigitalInput headSensor,
            @NotNull DigitalInput abSensor,
            @NotNull DigitalInput tailSensor
    ) {
        this.intakeMotor = intakeMotor;
        this.indexMotor = indexMotor;
        this.magazineMotor = magazineMotor;
        this.leftShooter = leftShooter;
        this.rightShooter = rightShooter;
        this.headSensor = headSensor;
        this.abSensor = abSensor;
        this.tailSensor = tailSensor;
    }

    public double getIndexCount() {
        return indexCount;
    }

    private boolean headSensorValue() {
        return !headSensor.get();
    }

    private boolean abdomenSensorValue() {
        return !abSensor.get();
    }

    private boolean tailSensorValue() {
        return !tailSensor.get();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Sensor", getSensorSwitch());
    }

    public void runIntakeMotor(double value) {
        intakeMotor.set(value);
    }

    public void runIndexMotor(double value) {
        indexMotor.set(value);
    }

    public void runSyncIntdex(double value) {
        intakeMotor.set(value);
        indexMotor.set(value);
    }

    public void runMagMotor(double value) {
        magazineMotor.set(-value);
    }

    public void runSyncMagIndex(double value) {
        indexMotor.set(value);
        magazineMotor.set(-value);
    }

    public void runShooter(double value) {
        leftShooter.set(value);
        rightShooter.set(value);
    }

    public void stopMotors() {
        intakeMotor.stopMotor();
        indexMotor.stopMotor();
        magazineMotor.stopMotor();
        leftShooter.stopMotor();
        rightShooter.stopMotor();
    }

    public int getSensorSwitch() {
        int head = headSensorValue() ? 0x1 : 0;
        int abdomen = abdomenSensorValue() ? 0x2 : 0;
        int tail = tailSensorValue() ? 0x4 : 0;
        return head | abdomen | tail;
    }

    public double incrementIndexCount() {
        indexCount += 1.0;
        return indexCount;
    }

    public void resetIndexCount() {
        indexCount = 0.0;
    }
}
