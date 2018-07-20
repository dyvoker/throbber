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

@SuppressWarnings("unused")
public class ThrobberView extends View {

	@NonNull
	private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	@NonNull
	private final RectF circleRect = new RectF();

	@Nullable
	private CircleDrawable circleDrawable;
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
		normalizedAnimator.setDuration(ThrobberMath.DEFAULT_CYCLE_DURATION);
		normalizedAnimator.setRepeatCount(ValueAnimator.INFINITE);

		rotationAnimator = ValueAnimator.ofFloat(0.0f, 360.0f);
		rotationAnimator.setInterpolator(null);
		rotationAnimator.setDuration(ThrobberMath.DEFAULT_ROTATION_CYCLE_DURATION);
		rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);

		normalizedAnimator.start();
		rotationAnimator.start();
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		size = Math.min(w, h);
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		// Draw bar circle background if needed.
		if (circleDrawable != null) {
			circleDrawable.draw(canvas);
		}
		// Calculate padding for stroke of the bar.
		float barPadding = barPaint.getStrokeWidth() / 2.0f + 1.0f;
		float circleWithPaddingSize = size - barPadding;
		circleRect.set(barPadding, barPadding, circleWithPaddingSize, circleWithPaddingSize);
		float value = (float) normalizedAnimator.getAnimatedValue();
		float rotationAngle = (float) rotationAnimator.getAnimatedValue();
		float startAngle = rotationAngle + ThrobberMath.calcStartAngle(value);
		float sweepAngle = ThrobberMath.calcSweepAngle(value);
		canvas.drawArc(circleRect, startAngle, sweepAngle, false, barPaint);

		// Repeat redraw while visible.
		if (getVisibility() == VISIBLE) {
			invalidate();
		}
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == VISIBLE) {
			// Start redraws.
			invalidate();
		}
	}

	/**
	 * @param width Width of a bar.
	 */
	public void setBarWidth(float width) {
		barPaint.setStrokeWidth(width);
	}

	/**
	 * @param color Color of a bar.
	 */
	public void setBarColor(@ColorInt int color) {
		barPaint.setColor(color);
	}

	/**
	 * @param color Color of a bar (by id).
	 */
	public void setBarColorRes(@ColorRes int color) {
		barPaint.setColor(getResources().getColor(color));
	}

	/**
	 * @param duration Duration of full cycle of bar animation in milliseconds.
	 */
	public void setCycleDuration(long duration) {
		if (duration < 10) {
			duration = ThrobberMath.DEFAULT_CYCLE_DURATION;
		}
		normalizedAnimator.setDuration(duration);
	}

	/**
	 * @param duration Duration of full cycle of bar rotation in milliseconds.
	 */
	public void setRotationCycleDuration(long duration) {
		if (duration < 10) {
			duration = ThrobberMath.DEFAULT_ROTATION_CYCLE_DURATION;
		}
		rotationAnimator.setDuration(duration);
	}

	/**
	 * Show default circle background (white circle with shadow).
	 */
	public void showCircleBackground() {
		circleDrawable = new CircleDrawable();
		invalidate();
	}

	/**
	 * @param circleDrawable Custom circle background.
	 */
	public void setCircleBackground(@NonNull CircleDrawable circleDrawable) {
		this.circleDrawable = circleDrawable;
		invalidate();
	}
}
