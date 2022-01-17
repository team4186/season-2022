package frc.subsystems.drive.motorfactory;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface MotorFactory {
    MotorController create(int main, int left, int right);
}
