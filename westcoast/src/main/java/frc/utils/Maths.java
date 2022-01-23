package frc.utils;

import static java.lang.Double.min;
import static java.lang.Math.*;

public class Maths {
    public static double attenuate(double input) {
        return copySign(pow(abs(input), 1.3), input);
    }

    public static double clamp(double input, double clamp) {
        return max(input, min(input, clamp));
    }

    public static double deadband(double input, double deadzone) {
        return input > -deadzone && input < deadzone ? 0.0 : input;
    }
}
