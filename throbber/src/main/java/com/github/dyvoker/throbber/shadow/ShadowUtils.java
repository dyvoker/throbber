package com.github.dyvoker.throbber.shadow;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.github.dyvoker.throbber.DensityConverter;

/**
 * Static methods for work with shadows.
 */
@SuppressWarnings("WeakerAccess")
public class ShadowUtils {

	/**
	 * Create copy of bitmap with shadow on it.
	 *
	 * @param bitmap Original bitmap.
	 * @param densityConverter Converter of Density Independence Pixels.
	 * @param shadowColor Color of a shadow.
	 * @param shadowRadiusDp Blur-radius of a shadow corner (dp).
	 * @param offsetXDp Shadow translation by X-axis (dp).
	 * @param offsetYDp Shadow translation by Y-axis (dp).
	 * @return Copy of original bitmap with shadow on it.
	 */
	@NonNull
	public static Bitmap addShadow(
		@NonNull Bitmap bitmap,
		@NonNull DensityConverter densityConverter,
		@ColorInt int shadowColor,
		float shadowRadiusDp,
		float offsetXDp,
		float offsetYDp
	) {
		return addShadow(
			bitmap,
			densityConverter,
			bitmap.getWidth(),
			bitmap.getHeight(),
			shadowColor,
			shadowRadiusDp,
			offsetXDp,
			offsetYDp
		);
	}

	/**
	 * Creates copy of bitmap with shadow on it.
	 * Can scale output bitmap to size = [destWidth x destHeight].
	 *
	 * @param bitmap Original bitmap.
	 * @param densityConverter Converter of Density Independence Pixels.
	 * @param destWidth Output bitmap width.
	 * @param destHeight Output bitmap height.
	 * @param shadowColor Color of a shadow.
	 * @param shadowRadiusDp Blur-radius of a shadow corner (dp).
	 * @param offsetXDp Shadow translation by X-axis (dp).
	 * @param offsetYDp Shadow translation by Y-axis (dp).
	 * @return Copy of original bitmap with shadow on it.
	 */
	@NonNull
	public static Bitmap addShadow(
		@NonNull Bitmap bitmap,
		@NonNull DensityConverter densityConverter,
		int destWidth,
		int destHeight,
		@ColorInt int shadowColor,
		float shadowRadiusDp,
		float offsetXDp,
		float offsetYDp
	) {
		// Convert all dp dimensions to px.
		float shadowRadius = densityConverter.dpToPx(shadowRadiusDp);
		float offsetY = densityConverter.dpToPx(offsetYDp);
		float offsetX = densityConverter.dpToPx(offsetXDp);

		// Create matrix for scale.
		final Matrix scaleToFitMatrix = new Matrix();
		if (bitmap.getWidth() != destWidth || bitmap.getHeight() != destHeight) {
			// Scale matrix need to calculate only if sizes of original and output bitmaps are not equals.
			final RectF source =
				new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF destination =
				new RectF(0, 0, destWidth, destHeight);
			scaleToFitMatrix.setRectToRect(source, destination, Matrix.ScaleToFit.CENTER);
		}

		// Matrix for drawing shadow.
		final Matrix dropShadowMatrix = new Matrix(scaleToFitMatrix);
		dropShadowMatrix.postTranslate(offsetX, offsetY);

		// Create bitmap for mask of original image.
		final Bitmap maskBitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ALPHA_8);
		final Canvas maskCanvas = new Canvas(maskBitmap);
		maskCanvas.drawBitmap(bitmap, dropShadowMatrix, null);

		// Create blur-paint with shadow color.
		final BlurMaskFilter filter = new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL);
		final Paint blurPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		blurPaint.setColor(shadowColor);
		blurPaint.setMaskFilter(filter);
		blurPaint.setFilterBitmap(true);

		final Bitmap result = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
		final Canvas resultCanvas = new Canvas(result);
		// Drawing blurred mask with color, then original bitmap over it.
		resultCanvas.drawBitmap(maskBitmap, 0,  0, blurPaint);
		resultCanvas.drawBitmap(bitmap, scaleToFitMatrix, null);
		maskBitmap.recycle();
		return result;
	}
}