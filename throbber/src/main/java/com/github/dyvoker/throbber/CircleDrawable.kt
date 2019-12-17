package com.github.dyvoker.throbber

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.github.dyvoker.throbber.shadow.CanvasWithShadow
import kotlin.math.max
import kotlin.math.min

/**
 * Draw circle with shadow (optional).
 *
 * @param color Color of circular background.
 * @param densityConverter Converter of Density Independence Pixels.
 * @param shadowColor Color of a shadow.
 * @param shadowRadiusDp Blur-radius of a shadow corner (dp).
 * @param offsetXDp Shadow translation by X-axis (dp).
 * @param offsetYDp Shadow translation by Y-axis (dp).
 */
class CircleDrawable @JvmOverloads constructor(
        @ColorInt color: Int,
        private val densityConverter: DensityConverter,
        @ColorInt private val shadowColor: Int = -0x80000000,
        private val shadowRadiusDp: Float = 2f,
        private val offsetXDp: Float = 0f,
        private val offsetYDp: Float = 2f
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        this.color = color
    }
    private var shadow: CanvasWithShadow? = null

    /**
     * Default constructor.
     * @param densityConverter Converter of Density Independence Pixels.
     */
    constructor(densityConverter: DensityConverter) : this(Color.WHITE, densityConverter)

    override fun draw(canvas: Canvas) {
        if (shadow != null) { // Draw cached image.
            shadow!!.draw(
                    canvas, shadowColor, shadowRadiusDp,
                    offsetXDp, offsetYDp, !shadow!!.isSameSize(canvas)
            )
            return
        }
        shadow = CanvasWithShadow(canvas, densityConverter)
        val tempCanvas = shadow!!.canvas
        // Draw primitives.
        val shadowPadding = densityConverter
                .dpToPx(shadowRadiusDp + max(offsetXDp, offsetYDp)) + 2
        val radius = min(bounds.width() / 2, bounds.height() / 2) - shadowPadding
        tempCanvas.drawCircle(radius + shadowPadding, radius + shadowPadding, radius, paint)
        // Draw shadow.
        shadow!!.draw(
                canvas, shadowColor, shadowRadiusDp,
                offsetXDp, offsetYDp, !shadow!!.isSameSize(canvas)
        )
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}