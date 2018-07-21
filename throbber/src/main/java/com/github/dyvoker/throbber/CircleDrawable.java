package com.github.dyvoker.throbber;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;

import com.github.dyvoker.shadow_lib.CanvasWithShadow;

/**
 * Draw circle with shadow (optional).
 */
public class CircleDrawable extends Drawable {

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	@ColorInt
	private final int shadowColor;
	private final float shadowRadiusDp;
	private final float offsetXDp;
	private final float offsetYDp;

	private CanvasWithShadow shadow;

	/**
	 * Default constructor.
	 */
	public CircleDrawable() {
		this(Color.WHITE);
	}

	/**
	 * @param color Color of circular background.
	 */
	public CircleDrawable(@ColorInt int color) {
		this(color, 0x80000000, 4, 2, 2);
	}

	/**
	 * @param color Color of circular background.
	 * @param shadowColor Color of a shadow.
	 * @param shadowRadiusDp Blur-radius of a shadow corner (dp).
	 * @param offsetXDp Shadow translation by X-axis (dp).
	 * @param offsetYDp Shadow translation by Y-axis (dp).
	 */
	public CircleDrawable(
		@ColorInt int color,
		@ColorInt int shadowColor,
		float shadowRadiusDp,
		float offsetXDp,
		float offsetYDp
	) {
		paint.setColor(color);
		this.shadowColor = shadowColor;
		this.shadowRadiusDp = shadowRadiusDp;
		this.offsetXDp = offsetXDp;
		this.offsetYDp = offsetYDp;
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		if (shadow != null) {
			// Draw cached image.
			shadow.draw(
				canvas, shadowColor, shadowRadiusDp,
				offsetXDp, offsetYDp, !shadow.isSameSize(canvas)
			);
			return;
		}
		shadow = new CanvasWithShadow(canvas);
		Canvas tempCanvas = shadow.getCanvas();

		// Draw primitives.
		float shadowPadding = dpToPx(shadowRadiusDp + Math.max(offsetXDp, offsetYDp)) + 2;
		float radius = Math.min(canvas.getWidth() / 2, canvas.getHeight() / 2) - shadowPadding;
		tempCanvas.drawCircle(radius + shadowPadding, radius + shadowPadding, radius, paint);

		// Draw shadow.
		shadow.draw(
			canvas, shadowColor, shadowRadiusDp,
			offsetXDp, offsetYDp, !shadow.isSameSize(canvas)
		);
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
}
