package crazygmr101.elytratilt;

import java.util.Arrays;

public class Smoother {
    private final float[] differences = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    private float last_yaw = 0;
    private final float CLAMP = 10;
    private boolean cleared = true;

    public Smoother() {
    }

    public float add_yaw(float new_yaw) {
        cleared = false;
        float difference = new_yaw - last_yaw;
        float sum = new_yaw;
        for (int i = 0; i < differences.length - 1; i++) {
            differences[i] = differences[i + 1];
            sum += differences[i];
        }
        differences[differences.length - 1] = difference;
        last_yaw = new_yaw;
        return Math.max(-CLAMP, Math.min(CLAMP, sum / differences.length));
    }

    public void clear() {
        if (!cleared) {
            Arrays.fill(differences, 0);
            cleared = true;
        }
    }
}
