package crazygmr101.elytratilt;

import java.util.Arrays;

public class Smoother {
    private static final double[] differences = new double[40];
    private static float last_yaw = 0;
    private static boolean cleared = true;
    private static float sum;
    private static float correction = 0;
    public static float correction_delta = 0;

    public static Float previous_valid_yaw = null;

    public static void add_yaw(float new_yaw) {
        cleared = false;
        //new_yaw = Math.round(new_yaw);
        new_yaw %= 360;
        correction = new_yaw;
        float difference = new_yaw - last_yaw;
        if (Math.abs(difference) < 100) {
            sum = new_yaw;
            for (int i = 0; i < differences.length - 1; i++) {
                differences[i] = differences[i + 1];
                sum += differences[i];
            }
            differences[differences.length - 1] = difference;
        }
        last_yaw = new_yaw;
    }

    public static float get_new_yaw() {
        float try_yaw = Math.max(
                -Configs.tilt_clamp,
                Math.min(
                        Configs.tilt_clamp,
                        correction_delta + sum / differences.length * Configs.tilt_factor
                                - correction / (Configs.tilt_factor * 2.1f)
                )
        );
        if (previous_valid_yaw == null) {
            previous_valid_yaw = try_yaw;
            return try_yaw;
        }
        if (
                (try_yaw == 0) || (
                        (Math.abs(previous_valid_yaw - try_yaw) < 10 && previous_valid_yaw / try_yaw >= 0) ||
                                (Math.abs(previous_valid_yaw - try_yaw) < 20 && previous_valid_yaw / try_yaw < 0)
                )
        ) {
            previous_valid_yaw = try_yaw;
            return try_yaw;
        }
        return previous_valid_yaw;
    }

    public static void clear() {
        if (!cleared) {
            Arrays.fill(differences, 0);
            cleared = true;
            previous_valid_yaw = null;
        }
        // correction_delta = get_new_yaw() - correction_delta;
    }
}
