package frc.utils;

import static frc.utils.Maths.attenuate;
import static frc.utils.Maths.deadband;

public class JoystickUtils {
    public static double clean(double input, double deadband) {
        return attenuate(deadband(input, deadband));
    }
}
