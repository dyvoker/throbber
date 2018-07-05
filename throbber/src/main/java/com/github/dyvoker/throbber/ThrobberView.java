package com.github.dyvoker.throbber;

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

	@NonNull
	private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	@NonNull
	private final RectF circleRect = new RectF();

	private int size;

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
		canvas.drawArc(circleRect, 0, 152, false, barPaint);
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
