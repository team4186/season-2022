package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
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
    private final WPI_TalonSRX leftShooter;
    @NotNull
    private final WPI_TalonSRX rightShooter;
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
            @NotNull WPI_TalonSRX leftShooter,
            @NotNull WPI_TalonSRX rightShooter,
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

    public void shooterTune() {
        setShooter(leftShooter, false);
        setShooter(rightShooter, true);
    }

    private void setShooter(WPI_TalonSRX shooter, boolean inverted) {
        shooter.configNominalOutputForward(0.0);
        shooter.configNominalOutputReverse(0.0);
        shooter.configPeakOutputForward(100.0);
        shooter.configPeakOutputReverse(-100.0);
        shooter.setNeutralMode(NeutralMode.Coast);
        shooter.config_kP(0, 0.1);
        shooter.config_kI(0, 0.4);
        shooter.config_kD(0, 0.0);
        shooter.setInverted(inverted);
    }

    public void runShooterCC(double value) {
        amps = value * 9;
        leftShooter.set(ControlMode.Current, amps);
        rightShooter.set(ControlMode.Current, amps);
    }

    public void publishCurrentLevels() {
        SmartDashboard.putNumber("Left Current", leftShooter.getSupplyCurrent());
        SmartDashboard.putNumber("Right Current", rightShooter.getSupplyCurrent());
    }

    private void runShooterPercent(double value) {
        percent = value;
        leftShooter.set(ControlMode.PercentOutput, value);
        rightShooter.set(ControlMode.PercentOutput, value);
    }

    public void publishMotorOutputPercent() {
        SmartDashboard.putNumber("Left Current", leftShooter.getMotorOutputPercent());
        SmartDashboard.putNumber("Right Current", rightShooter.getMotorOutputPercent());
    }

    public boolean shooterEndCC() {
        return leftShooter.getSupplyCurrent() == amps && rightShooter.getSupplyCurrent() == amps;
    }

    public boolean shooterEndPercent() {
        return leftShooter.getMotorOutputPercent() == percent && rightShooter.getMotorOutputPercent() == percent;
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
