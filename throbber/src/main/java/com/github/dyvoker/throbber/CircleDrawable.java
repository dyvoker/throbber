package com.github.dyvoker.throbber;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Draw circle with shadow (optional).
 */
public class CircleDrawable extends Drawable {

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public CircleDrawable() {
		this(Color.WHITE);
	}

	public CircleDrawable(@ColorInt int color) {
		paint.setColor(color);
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		float centerX = canvas.getWidth() / 2;
		float centerY = canvas.getHeight() / 2;
		float radius = Math.min(centerX, centerY);
		canvas.drawCircle(centerX, centerY, radius, paint);
	}

	@Override
	public void setAlpha(int alpha) {
		paint.setAlpha(alpha);
		invalidateSelf();
	}

	@Override
	public void setColorFilter(@Nullable ColorFilter colorFilter) {
		paint.setColorFilter(colorFilter);
		invalidateSelf();
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}
}
