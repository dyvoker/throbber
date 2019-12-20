package com.github.dyvoker.throbber.shadow

import android.graphics.*
import androidx.annotation.ColorInt
import com.github.dyvoker.throbber.DensityConverter

/**
 * Static methods for work with shadows.
 */
@Suppress("MemberVisibilityCanBePrivate")
object ShadowUtils {
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
    fun addShadow(
            bitmap: Bitmap,
            densityConverter: DensityConverter,
            @ColorInt shadowColor: Int,
            shadowRadiusDp: Float,
            offsetXDp: Float,
            offsetYDp: Float
    ): Bitmap = addShadow(
            bitmap,
            densityConverter,
            bitmap.width,
            bitmap.height,
            shadowColor,
            shadowRadiusDp,
            offsetXDp,
            offsetYDp
    )

    /**
     * Creates copy of bitmap with shadow on it.
     * Can scale output bitmap to size = [destWidth] x [destHeight].
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
    fun addShadow(
            bitmap: Bitmap,
            densityConverter: DensityConverter,
            destWidth: Int,
            destHeight: Int,
            @ColorInt shadowColor: Int,
            shadowRadiusDp: Float,
            offsetXDp: Float,
            offsetYDp: Float
    ): Bitmap { // Convert all dp dimensions to px.
        val shadowRadius = densityConverter.dpToPx(shadowRadiusDp)
        val offsetY = densityConverter.dpToPx(offsetYDp)
        val offsetX = densityConverter.dpToPx(offsetXDp)
        // Create matrix for scale.
        val scaleToFitMatrix = Matrix()
        if (bitmap.width != destWidth || bitmap.height != destHeight) { // Scale matrix need to calculate only if sizes of original and output bitmaps are not equals.
            val source = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
            val destination = RectF(0f, 0f, destWidth.toFloat(), destHeight.toFloat())
            scaleToFitMatrix.setRectToRect(source, destination, Matrix.ScaleToFit.CENTER)
        }
        // Matrix for drawing shadow.
        val dropShadowMatrix = Matrix(scaleToFitMatrix)
        dropShadowMatrix.postTranslate(offsetX, offsetY)
        // Create bitmap for mask of original image.
        val maskBitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ALPHA_8)
        val maskCanvas = Canvas(maskBitmap)
        maskCanvas.drawBitmap(bitmap, dropShadowMatrix, null)
        // Create blur-paint with shadow color.
        val filter = BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL)
        val blurPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blurPaint.color = shadowColor
        blurPaint.maskFilter = filter
        blurPaint.isFilterBitmap = true
        val result = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888)
        val resultCanvas = Canvas(result)
        // Drawing blurred mask with color, then original bitmap over it.
        resultCanvas.drawBitmap(maskBitmap, 0f, 0f, blurPaint)
        resultCanvas.drawBitmap(bitmap, scaleToFitMatrix, null)
        maskBitmap.recycle()
        return result
    }
}