package frc.subsystems.drive.motorfactory;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class MotorFactoryHybrid implements MotorFactory {
    public MotorController create(int main, int left, int right) {
        WPI_TalonSRX motorMain = new WPI_TalonSRX(main);
        WPI_VictorSPX motorLeft = new WPI_VictorSPX(left);
        WPI_VictorSPX motorRight = new WPI_VictorSPX(right);

        motorRight.setInverted(true);

        motorLeft.follow(motorMain);
        motorRight.follow(motorMain);

        motorMain.configContinuousCurrentLimit(18);
        motorMain.configPeakCurrentLimit(20);
        motorMain.configPeakCurrentDuration(45);
        motorMain.enableCurrentLimit(true);
        motorMain.setNeutralMode(NeutralMode.Brake);

        return motorMain;
    }
}
