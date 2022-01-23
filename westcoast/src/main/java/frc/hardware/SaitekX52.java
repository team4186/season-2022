package frc.hardware;

@SuppressWarnings("unused")
public interface SaitekX52 {
    interface Axis {
        int X = 0;
        int Y = 1;
        int THROTTLE = 2;
        int ROTARY_0 = 3;
        int ROTARY_1 = 4;
        int TWIST = 5;
        int SLIDER = 6;

        int AXES_COUNT = 7;
    }

    interface Pov {
        int STICK_POV = 0;
        int STICK_HAT = 1; // fake
        int THROTTLE_HAT = 2; // fake

        int POV_COUNT = 3;
    }


    interface Direction {
        int UP = 0;
        int RIGHT = 90;
        int DOWN = 180;
        int LEFT = 270;
    }

    // NOTE remember to add 1 to keep FRC compatibility
    interface Buttons {
        int TRIGGER = 0;
        int LAUNCH = 1;
        int FIRE_A = 2;
        int FIRE_B = 3;
        int FIRE_C = 4;
        int PINKIE = 5;
        int FIRE_D = 6;
        int FIRE_E = 7;
        int TOGGLE_1 = 8;
        int TOGGLE_2 = 9;
        int TOGGLE_3 = 10;
        int TOGGLE_4 = 11;
        int TOGGLE_5 = 12;
        int TOGGLE_6 = 13;
        int SECOND_TRIGGER = 14;
        int MOUSE_FIRE = 15;
        int WHEEL_SCROLLDOWN = 16;
        int WHEEL_SCROLLUP = 17;
        int WHEEL_PRESS = 18;

        int JOY_HAT_UP = 19;
        int JOY_HAT_RIGHT = 20;
        int JOY_HAT_DOWN = 21;
        int JOY_HAT_LEFT = 22;

        int THROTTLE_HAT_UP = 23;
        int THROTTLE_HAT_RIGHT = 24;
        int THROTTLE_HAT_DOWN = 25;
        int THROTTLE_HAT_LEFT = 26;

        int MODE_1 = 27;
        int MODE_2 = 28;
        int MODE_3 = 29;

        int CLUTCH = 30;
        int FIRE_I = 30;

        int TIMER_PG_PRESS = 31;

        // TODO check if anything beyond the Button 32 is reported
        int TIMER_STARTSTOP = 32;
        int TIMER_RESET = 33;
        int TIMER_PG_UP = 34;
        int TIMER_PG_DOWN = 35;

        int TIMER_ARROW_UP = 36;
        int TIMER_ARROW_DOWN = 37;
        int TIMER_ARROW_PRESS = 38;

        int BUTTON_COUNT = 39;
    }

    interface Mode {
        int RED = 0;
        int PURPLE = 1;
        int BLUE = 2;
    }
}
