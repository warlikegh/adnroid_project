package com.zzz.technoparkmobileproject.auth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import static java.lang.Math.sin;


class SnowFlake {
    private static final double HALF_PI = (double) Math.PI / 2;
    private static final double INCREMENT_LOWER = 2;
    private static final double INCREMENT_UPPER = 3;
    private static final double FLAKE_SIZE_LOWER = 7;
    private static final double FLAKE_SIZE_UPPER = 20;

    private static RandomSnow random = new RandomSnow();
    public final Point position;
    private double angle;
    private final double increment;
    private final double flakeSize;
    private final Paint paint;

    public static SnowFlake create(int width, int height, Paint paint) {
        RandomSnow random = new RandomSnow();
        int x = random.getRandom(width);
        int y = random.getRandom(height);
        Point position = new Point(x, y);
        double angle = random.getRandom(HALF_PI * 4);
        double increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        double flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        return new SnowFlake(random, position, angle, increment, flakeSize, paint);
    }

    SnowFlake(RandomSnow random,
              Point position,
              double angle,
              double increment,
              double flakeSize,
              Paint paint) {
        this.random = random;
        this.position = position;
        this.angle = angle;
        this.increment = increment;
        this.flakeSize = flakeSize;
        this.paint = paint;
    }

    private void move(int width, int height) {
        double x = position.x + (increment * Math.cos(angle));
        double y = position.y + (increment * sin(angle));

        if (x < flakeSize + 1 || y < flakeSize + 1) {
            position.set((int) x + 1, (int) y + 1);
        } else {
            position.set((int) x, (int) y);
        }
        if (!isInside(width, height)) {
            reset(width, height);
        }
    }

    private boolean isInside(int width, int height) {
        int x = position.x;
        int y = position.y;
        return x > flakeSize && x + flakeSize < width && y > flakeSize && y + flakeSize < height;
    }

    private void reset(int width, int height) {
        if (position.x < flakeSize || position.x + flakeSize > width) {
            if (angle < 0)
                angle = -2 * HALF_PI - angle;
            else
                angle = 2 * HALF_PI - angle;
        } else if (position.y < flakeSize || position.y + flakeSize > height)
            angle *= -1;
    }

    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        move(width, height);
        canvas.drawCircle(position.x, position.y, (float) flakeSize, paint);
    }
}
