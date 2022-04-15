package frc.hardware

object SaitekX52 {
    object Axis {
        const val X = 0
        const val Y = 1
        const val THROTTLE = 2
        const val ROTARY_0 = 3
        const val ROTARY_1 = 4
        const val TWIST = 5
        const val SLIDER = 6
        const val AXES_COUNT = 7
    }

    object Pov {
        const val STICK_POV = 0
        const val STICK_HAT = 1 // fake
        const val THROTTLE_HAT = 2 // fake
        const val POV_COUNT = 3
    }

    object Direction {
        const val UP = 0
        const val RIGHT = 90
        const val DOWN = 180
        const val LEFT = 270
    }

    object Buttons {
        const val TRIGGER = 1
        const val LAUNCH = 2
        const val FIRE_A = 3
        const val FIRE_B = 4
        const val FIRE_C = 5
        const val PINKIE = 6
        const val FIRE_D = 7
        const val FIRE_E = 8
        const val TOGGLE_1 = 9
        const val TOGGLE_2 = 10
        const val TOGGLE_3 = 11
        const val TOGGLE_4 = 12
        const val TOGGLE_5 = 13
        const val TOGGLE_6 = 14
        const val SECOND_TRIGGER = 15
        const val MOUSE_FIRE = 16
        const val WHEEL_SCROLLDOWN = 17
        const val WHEEL_SCROLLUP = 18
        const val WHEEL_PRESS = 19
        const val JOY_HAT_UP = 20
        const val JOY_HAT_RIGHT = 21
        const val JOY_HAT_DOWN = 22
        const val JOY_HAT_LEFT = 23
        const val THROTTLE_HAT_UP = 24
        const val THROTTLE_HAT_RIGHT = 25
        const val THROTTLE_HAT_DOWN = 26
        const val THROTTLE_HAT_LEFT = 27
        const val MODE_1 = 28
        const val MODE_2 = 29
        const val MODE_3 = 30
        const val CLUTCH = 31
        const val FIRE_I = 31
        const val TIMER_PG_PRESS = 32

        // TODO check if anything beyond the Button 32 is reported
        const val TIMER_STARTSTOP = 33
        const val TIMER_RESET = 34
        const val TIMER_PG_UP = 35
        const val TIMER_PG_DOWN = 36
        const val TIMER_ARROW_UP = 37
        const val TIMER_ARROW_DOWN = 38
        const val TIMER_ARROW_PRESS = 39
        const val BUTTON_COUNT = 40
    }

    object Mode {
        const val RED = 0
        const val PURPLE = 1
        const val BLUE = 2
    }
}