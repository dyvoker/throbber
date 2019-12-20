package com.github.dyvoker.throbber.shadow

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import com.github.dyvoker.throbber.DensityConverter

/**
 * Helping class for working with canvas.
 * Get [Canvas] and manually draw on it,
 * then call [draw].
 * It automatically draw canvas content with shadow to another canvas.
 *
 * @param width Custom width of canvas.
 * @param height Custom height of canvas.
 * @param config Config of bitmap to draw on.
 * @param densityConverter Converter for Density Independence Pixels.
 */
@Suppress("unused")
class CanvasWithShadow(
        width: Int,
        height: Int,
        config: Bitmap.Config,
        private val densityConverter: DensityConverter
) {

    private val canvasBitmap: Bitmap = Bitmap.createBitmap(width, height, config)

    /**
     * @return Special canvas. You must draw on it,
     * it needs for working a method [draw].
     */
    val canvas: Canvas = Canvas(canvasBitmap)
    private var bitmapWithShadow : Bitmap? = null // Cached bitmap with shadow.

    /**
     * @param canvas Canvas of view when you want to draw.
     * @param densityConverter Converter for Density Independence Pixels.
     */
    constructor(
            canvas: Canvas,
            densityConverter: DensityConverter
    ) : this(canvas.width, canvas.height, Bitmap.Config.ARGB_8888, densityConverter)

    /**
     * @param canvas Canvas of view when you want to draw.
     * @param config Config of bitmap to draw on.
     * @param densityConverter Converter for Density Independence Pixels.
     */
    constructor(
            canvas: Canvas,
            config: Bitmap.Config,
            densityConverter: DensityConverter
    ) : this(canvas.width, canvas.height, config, densityConverter)

    /**
     * @param width Custom width of canvas.
     * @param height Custom height of canvas.
     * @param densityConverter Converter for Density Independence Pixels.
     */
    constructor(
            width: Int,
            height: Int,
            densityConverter: DensityConverter
    ) : this(width, height, Bitmap.Config.ARGB_8888, densityConverter)

    /**
     * Draw canvas content on another canvas and add shadow by parameters.
     *
     * @param canvas Canvas to draw content with shadow/
     * @param shadowColor Color of a shadow.
     * @param shadowRadiusDp Blur-radius of a shadow corner (dp).
     * @param offsetXDp Shadow translation by X-axis (dp).
     * @param offsetYDp Shadow translation by Y-axis (dp).
     * @param forceRedraw If true - redraw cached bitmap with shadow.
     */
    fun draw(
            canvas: Canvas,
            @ColorInt shadowColor: Int,
            shadowRadiusDp: Float,
            offsetXDp: Float,
            offsetYDp: Float,
            forceRedraw: Boolean
    ) { // Redraw cached image, only if it needed.
        if (bitmapWithShadow == null || forceRedraw) {
            if (bitmapWithShadow != null) {
                bitmapWithShadow!!.recycle()
            }
            bitmapWithShadow = ShadowUtils.addShadow(
                    canvasBitmap,
                    densityConverter,
                    shadowColor,
                    shadowRadiusDp,
                    offsetXDp,
                    offsetYDp
            )
            canvas.drawBitmap(bitmapWithShadow, 0f, 0f, null)
            return
        }
        canvas.drawBitmap(bitmapWithShadow, 0f, 0f, null)
    }

    /**
     * @param canvas Canvas to compare with.
     * @return Return true - if cached bitmap with shadow have same width and height as canvas.
     */
    fun isSameSize(canvas: Canvas): Boolean = isSameSize(canvas.width, canvas.height)

    /**
     * @param width Width to compare with.
     * @param height Height to compare with.
     * @return Return true - if cached bitmap with shadow have same width and height.
     */
    fun isSameSize(width: Int, height: Int): Boolean {
        if (bitmapWithShadow == null) {
            return false
        }
        val bitmapWidth = bitmapWithShadow!!.width
        val bitmapHeight = bitmapWithShadow!!.height
        return bitmapWidth == width && bitmapHeight == height
    }

    /**
     * Remove cached bitmap.
     */
    fun recycle() {
        if (bitmapWithShadow != null) {
            bitmapWithShadow!!.recycle()
        }
    }
}