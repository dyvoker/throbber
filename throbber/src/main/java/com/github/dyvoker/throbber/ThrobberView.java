package com.github.dyvoker.throbber;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ThrobberView extends View {

	private final static long DEFAULT_CYCLE_DELAY = 1500;
	private final static long DEFAULT_ROTATION_CYCLE_DELAY = 2140;

	@NonNull
	private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	@NonNull
	private final RectF circleRect = new RectF();

	private boolean isRunning = true;
	private int size;

	@NonNull
	private final ValueAnimator normalizedAnimator;
	@NonNull
	private final ValueAnimator rotationAnimator;


	public ThrobberView(Context context) {
		this(context, null);
	}

	public ThrobberView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ThrobberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		barPaint.setStyle(Paint.Style.STROKE);
		setBarColor(Color.BLUE);
		setBarWidth(20.0f);

		normalizedAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
		normalizedAnimator.setInterpolator(null); // Yes, linear interpolator.
		normalizedAnimator.setDuration(DEFAULT_CYCLE_DELAY);
		normalizedAnimator.setRepeatCount(ValueAnimator.INFINITE);
		normalizedAnimator.start();

		rotationAnimator = ValueAnimator.ofFloat(0.0f, 360.0f);
		rotationAnimator.setInterpolator(null);
		rotationAnimator.setDuration(DEFAULT_ROTATION_CYCLE_DELAY);
		rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
		rotationAnimator.start();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		size = Math.min(w, h);
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		// Calculate padding for stroke of the bar.
		float barPadding = barPaint.getStrokeWidth() / 2.0f + 1.0f;
		float circleWithPaddingSize = size - barPadding;
		circleRect.set(barPadding, barPadding, circleWithPaddingSize, circleWithPaddingSize);
		float value = (float) normalizedAnimator.getAnimatedValue();
		float rotationAngle = (float) rotationAnimator.getAnimatedValue();
		float startAngle = rotationAngle + ThrobberMath.calcStartAngle(value);
		float sweepAngle = ThrobberMath.calcSweepAngle(value);
		canvas.drawArc(circleRect, startAngle, sweepAngle, false, barPaint);
		if (isRunning) {
			invalidate();
		}
	}

	public void start() {
		isRunning = true;
		normalizedAnimator.start();
		rotationAnimator.start();
		invalidate();
	}

	public void pause() {
		isRunning = false;
		normalizedAnimator.end();
		rotationAnimator.end();
	}

	public void setBarWidth(float width) {
		barPaint.setStrokeWidth(width);
	}

	public void setBarColor(@ColorInt int color) {
		barPaint.setColor(color);
	}

	public void setBarColorRes(@ColorRes int color) {
		barPaint.setColor(getResources().getColor(color));
	}
}
