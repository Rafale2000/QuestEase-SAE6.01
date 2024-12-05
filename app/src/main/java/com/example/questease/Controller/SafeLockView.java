package com.example.questease.Controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class SafeLockView extends View {

    private Paint diskPaint;
    private Paint markPaint;
    private float currentAngle = 0f;
    private float targetAngle = 90f; // Angle cible
    private boolean isUnlocked = false;

    public SafeLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        diskPaint = new Paint();
        diskPaint.setColor(Color.GRAY);
        diskPaint.setStyle(Paint.Style.FILL);

        markPaint = new Paint();
        markPaint.setColor(Color.RED);
        markPaint.setStrokeWidth(10f);
    }

    public void updateRotation(float rotationSpeed) {
        if (!isUnlocked) {
            currentAngle += rotationSpeed * 5; // Ajuster la sensibilité
            currentAngle = (currentAngle + 360) % 360; // Reste dans [0, 360]
            invalidate();

            if (Math.abs(currentAngle - targetAngle) < 5) { // Tolérance de ±5°
                isUnlocked = true;
                post(() -> Toast.makeText(getContext(), "Solution trouvée !", Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 50;

        // Dessiner le disque
        canvas.drawCircle(centerX, centerY, radius, diskPaint);

        // Dessiner la marque cible
        float targetX = centerX + radius * (float) Math.cos(Math.toRadians(targetAngle));
        float targetY = centerY + radius * (float) Math.sin(Math.toRadians(targetAngle));
        canvas.drawLine(centerX, centerY, targetX, targetY, markPaint);

        // Dessiner la marque courante
        float currentX = centerX + radius * (float) Math.cos(Math.toRadians(currentAngle));
        float currentY = centerY + radius * (float) Math.sin(Math.toRadians(currentAngle));
        markPaint.setColor(Color.BLUE);
        canvas.drawLine(centerX, centerY, currentX, currentY, markPaint);
    }
}
