package frc.utils;

import static frc.utils.Maths.clamp;
import static frc.utils.Maths.deadband;

public class PidUtils {
    public static double clean(double input, double clamp, double deadband) {
        return clamp(deadband(input, deadband), clamp);
    }
}
