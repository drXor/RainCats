package com.minecats.cindyk.raincats;

/**
 * Mojang code yaaaay
 */

public class MathHelper {

    private static float[] cache = new float[65536];

    public static final float sin(float paramFloat) {

        return cache[((int) (paramFloat * 10430.378F) & 0xFFFF)];
    }

    public static final float cos(float paramFloat) {

        return cache[((int) (paramFloat * 10430.378F + 16384.0F) & 0xFFFF)];
    }

    static {
        for (int i = 0; i < cache.length; i++)
            cache[i] = ((float) Math.sin(i * Math.PI * 2.0D / cache.length));
    }

}
