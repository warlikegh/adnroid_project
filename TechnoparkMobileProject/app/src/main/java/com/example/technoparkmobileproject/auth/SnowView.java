package com.example.technoparkmobileproject.auth;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.technoparkmobileproject.R;

public class SnowView extends View {
    private static final int NUM_SNOWFLAKES = 40;
    private static final int DELAY = 5;
    private int red = 44;
    private int green = 43;
    private int blue = 41;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public void setRGB(int red,int green,int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
        paint.setColor(Color.argb(255, red, green, blue));
    }

    private SnowFlake[] snowflakes;

    public SnowView(Context context) {
        super(context);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ResourceAsColor")
    protected void resize(int width, int height) {

        //paint.setColor(Color.argb(255, 28, 28,28));
        //paint.setColor(Color.argb(255, 9, 9,9));
        // paint.setColor(Color.argb(255, 89, 148,204));
        //paint.setColor(Color.argb(255, 66, 99, 130));   //telega good
        paint.setColor(Color.argb(255, red, green, blue));
        // paint.setColor(R.color.color);
        // paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        snowflakes = new SnowFlake[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            snowflakes[i] = SnowFlake.create(width, height, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec)
        );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((w != oldw || h != oldh) && (w > 0 && h > 0)) {
            resize(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Double[][] modul = new Double[NUM_SNOWFLAKES][NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++)
            for (int j = i + 1; j < NUM_SNOWFLAKES; j++) {
                Double a = Math.sqrt((snowflakes[i].position.x - snowflakes[j].position.x) * (snowflakes[i].position.x - snowflakes[j].position.x) +
                        (snowflakes[i].position.y - snowflakes[j].position.y) * (snowflakes[i].position.y - snowflakes[j].position.y));
                if (a < 255.0) {
                    modul[i][j] = a;
                    if (a<150.0){
                        modul[i][j] +=100.0;
                    }
                } else {
                    modul[i][j] = 255.;
                }
            }
        for (SnowFlake snowFlake : snowflakes) {
            snowFlake.draw(canvas);

        }
        Paint paintLine = new Paint();
        paintLine.setColor(Color.rgb(red, green, blue));
        for (int i = 1; i < NUM_SNOWFLAKES; i++)
            for (int j = i + 1; j < NUM_SNOWFLAKES; j++) {
                paintLine.setAlpha(255 - modul[i][j].intValue());
                canvas.drawLine(snowflakes[i].position.x, snowflakes[i].position.y,
                        snowflakes[j].position.x, snowflakes[j].position.y, paintLine);
            }
        getHandler().postDelayed(runnable, DELAY);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
}
