package wangdaye.com.geometricweather.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.utils.DisplayUtils;

/**
 * Round progress.
 * */

public class RoundProgress extends View {

    private Paint progressPaint;

    private RectF backgroundRectF = new RectF();
    private RectF progressRectF = new RectF();

    private int progress;
    private int max;
    @ColorInt private int progressColor;
    @ColorInt private int backgroundColor;

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        initPaint();
    }

    private void initialize() {
        progress = 0;
        max = 100;
        progressColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        backgroundColor = ContextCompat.getColor(getContext(), R.color.colorLine);
    }

    private void initPaint() {
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress = getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public void setProgressColor(@ColorInt int progressColor) {
        this.progressColor = progressColor;
        this.invalidate();
    }

    public void setProgressBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int padding = (int) DisplayUtils.dpToPx(getContext(), 2);
        backgroundRectF.set(
                padding,
                padding,
                getMeasuredWidth() - padding,
                getMeasuredHeight() - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = backgroundRectF.height() / 2f;
        progressPaint.setColor(backgroundColor);
        progressPaint.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
        canvas.drawRoundRect(backgroundRectF, radius, radius, progressPaint);

        progressRectF.set(
                backgroundRectF.left,
                backgroundRectF.top,
                backgroundRectF.left + backgroundRectF.width() * progress / max,
                backgroundRectF.bottom);
        progressPaint.setColor(progressColor);
        progressPaint.setShadowLayer(
                1,
                0,
                1,
                Color.argb((int) (255 * 0.1), 0, 0, 0));
        if (progressRectF.width() < 2 * radius) {
            canvas.drawCircle(
                    progressRectF.left + radius,
                    progressRectF.top + radius,
                    radius,
                    progressPaint);
        } else {
            canvas.drawRoundRect(progressRectF, radius, radius, progressPaint);
        }
    }
}
