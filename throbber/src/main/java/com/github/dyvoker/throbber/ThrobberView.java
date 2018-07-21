package com.github.dyvoker.throbber;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

@SuppressWarnings("unused")
public class ThrobberView extends View {

	private final static float BASE_PADDING = dpToPx(10);
	private final static float DEFAULT_BAR_WIDTH = dpToPx(4);

	@NonNull
	private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	@NonNull
	private final RectF circleRect = new RectF();

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
		setBarColor(getThemeAccentColor(getContext()));
		setBarWidth(DEFAULT_BAR_WIDTH);

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
		size = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom());
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		// Calculate padding for stroke of the bar.
		float barPadding = barPaint.getStrokeWidth() / 2.0f + 1.0f + BASE_PADDING;
		float circleWithPaddingSize = size - barPadding;
		circleRect.set(
			getPaddingLeft() + barPadding,
			getPaddingTop() + barPadding,
			getPaddingLeft() + circleWithPaddingSize,
			getPaddingTop() + circleWithPaddingSize
		);
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
	 * You can create customized {@link CircleDrawable} and set it via {@link #setBackgroundDrawable}.
	 */
	public void showCircleBackground() {
		CircleDrawable circleDrawable = new CircleDrawable();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			setBackground(circleDrawable);
		} else {
			setBackgroundDrawable(circleDrawable);
		}
		invalidate();
	}

	/**
	 * Convert dp to px.
	 *
	 * @param dp Size in dp.
	 * @return Size in px.
	 */
	private static float dpToPx(float dp) {
		return TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP,
			dp,
			Resources.getSystem().getDisplayMetrics()
		);
	}

	private static int getThemeAccentColor(@NonNull Context context) {
		final TypedValue value = new TypedValue ();
		context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
		return value.data;
	}
}
