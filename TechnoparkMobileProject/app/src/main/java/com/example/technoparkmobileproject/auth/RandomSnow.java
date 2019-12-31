package com.example.technoparkmobileproject.auth;

public class RandomSnow {
    private static final java.util.Random RANDOM = new java.util.Random();

    public double getRandom(double lower, double upper) {
        double min = Math.min(lower, upper);
        double max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    public double getRandom(double upper) {
        return RANDOM.nextDouble() * upper;
    }

    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }
}
