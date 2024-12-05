package com.example.questease.Controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class MovingCircleView extends View {

    private float circleX = 0f, circleY = 0f;
    private float circleRadius = 50f;
    private float targetRadius = 30f;

    private Paint bluePaint, redPaint;
    private ArrayList<float[]> targets = new ArrayList<>();

    public MovingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        bluePaint = new Paint();
        bluePaint.setColor(0xFF0000FF); // Blue color
        bluePaint.setAntiAlias(true);

        redPaint = new Paint();
        redPaint.setColor(0xFFFF0000); // Red color
        redPaint.setAntiAlias(true);
    }

    public void setTargets(ArrayList<float[]> newTargets) {
        targets.clear();
        targets.addAll(newTargets);
        invalidate(); // Redraw the view
    }

    public void updateCirclePosition(float x, float y) {
        circleX = x * 50; // Scale motion for visibility
        circleY = y * 50;
        invalidate(); // Redraw the view
    }

    public float[] getCirclePosition() {
        return new float[]{circleX, circleY};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        // Draw the moving circle (blue)
        canvas.drawCircle(centerX + circleX, centerY + circleY, circleRadius, bluePaint);

        // Draw the targets (red)
        for (float[] target : targets) {
            canvas.drawCircle(target[0], target[1], targetRadius, redPaint);
        }
    }
}
