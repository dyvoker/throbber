package com.github.dyvoker.throbber;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dyvoker.android_utils.DpUtils;
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
		this(color, 0x80000000, 2, 0, 2);
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
		float shadowPadding = DpUtils.dpToPx(shadowRadiusDp + Math.max(offsetXDp, offsetYDp)) + 2;
		float radius = Math.min(getBounds().width() / 2, getBounds().height() / 2) - shadowPadding;
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
}
