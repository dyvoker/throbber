package com.github.dyvoker.throbber

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.github.dyvoker.throbber.ThrobberMath.calcStartAngle
import com.github.dyvoker.throbber.ThrobberMath.calcSweepAngle
import kotlin.math.min

@Suppress("MemberVisibilityCanBePrivate", "unused")
class ThrobberView(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
) : View(context, attrs, defStyleAttr) {

    private val densityConverter: DensityConverter = DensityConverter(resources.displayMetrics)
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circleRect = RectF()
    private var size = 0
    private var basePadding: Float
    private val normalizedAnimator: ValueAnimator
    private val rotationAnimator: ValueAnimator

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        size = min(w - paddingLeft - paddingRight, h - paddingTop - paddingBottom)
    }

    override fun onDraw(canvas: Canvas) { // Calculate padding for stroke of the bar.
        val barPadding = barPaint.strokeWidth / 2.0f + 1.0f + basePadding
        val circleWithPaddingSize = size - barPadding
        circleRect[paddingLeft + barPadding, paddingTop + barPadding, paddingLeft + circleWithPaddingSize] = paddingTop + circleWithPaddingSize
        val value = normalizedAnimator.animatedValue as Float
        val rotationAngle = rotationAnimator.animatedValue as Float
        val startAngle = rotationAngle + calcStartAngle(value)
        val sweepAngle = calcSweepAngle(value)
        canvas.drawArc(circleRect, startAngle, sweepAngle, false, barPaint)
        // Repeat redraw while visible.
        if (visibility == VISIBLE) {
            invalidate()
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) { // Start redraws.
            invalidate()
        }
    }

    /**
     * @param width Width of a bar.
     */
    fun setBarWidth(width: Float) {
        barPaint.strokeWidth = width
    }

    /**
     * @param color Color of a bar.
     */
    fun setBarColor(@ColorInt color: Int) {
        barPaint.color = color
    }

    /**
     * @param color Color of a bar (by id).
     */
    fun setBarColorRes(@ColorRes color: Int) {
        barPaint.color = ResourcesCompat.getColor(resources, color, null)
    }

    /**
     * @param duration Duration of full cycle of bar animation in milliseconds.
     */
    fun setCycleDuration(duration: Long) {
        var correctedDuration = duration
        if (correctedDuration < 10) {
            correctedDuration = ThrobberMath.DEFAULT_CYCLE_DURATION
        }
        normalizedAnimator.duration = correctedDuration
    }

    /**
     * @param duration Duration of full cycle of bar rotation in milliseconds.
     */
    fun setRotationCycleDuration(duration: Long) {
        var correctedDuration = duration
        if (correctedDuration < 10) {
            correctedDuration = ThrobberMath.DEFAULT_ROTATION_CYCLE_DURATION
        }
        rotationAnimator.duration = correctedDuration
    }

    /**
     * Show default circle background (white circle with shadow).
     * You can create customized [CircleDrawable] and set it via [.setBackgroundDrawable].
     */
    fun showCircleBackground() {
        val circleDrawable = CircleDrawable(densityConverter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = circleDrawable
        } else {
            setBackgroundDrawable(circleDrawable)
        }
        invalidate()
    }

    /**
     * @param basePadding Base padding of throbber bar from sides.
     */
    fun setBasePadding(basePadding: Float) {
        this.basePadding = basePadding
    }

    companion object {
        private const val BASE_PADDING_DP = 10f
        private const val DEFAULT_BAR_WIDTH_DP = 4f
        private const val DEFAULT_CIRCLE_BACKGROUND_COLOR = -0x1
        private const val DEFAULT_SHADOW_COLOR = -0x80000000
        private const val DEFAULT_SHADOW_RADIUS_DP = 2f
        private const val DEFAULT_SHADOW_OFFSET_X_DP = 0f
        private const val DEFAULT_SHADOW_OFFSET_Y_DP = 2f
        private fun getThemeAccentColor(context: Context): Int {
            val value = TypedValue()
            context.theme.resolveAttribute(R.attr.colorAccent, value, true)
            return value.data
        }
    }

    init {
        barPaint.style = Paint.Style.STROKE
        // Get attributes from xml layout.
        val arr = context.obtainStyledAttributes(attrs, R.styleable.ThrobberView)
        val barWidth = arr.getDimension(
                R.styleable.ThrobberView_throbber_barWidth,
                densityConverter.dpToPx(DEFAULT_BAR_WIDTH_DP)
        )
        setBarWidth(barWidth)
        val barColor = arr.getColor(
                R.styleable.ThrobberView_throbber_barColor,
                getThemeAccentColor(getContext())
        )
        setBarColor(barColor)
        val showCircleBackground = arr.getBoolean(
                R.styleable.ThrobberView_throbber_showCircleBackground,
                false
        )
        val circleBackgroundColor = arr.getColor(
                R.styleable.ThrobberView_throbber_circleBackgroundColor,
                DEFAULT_CIRCLE_BACKGROUND_COLOR
        )
        val shadowColor = arr.getColor(
                R.styleable.ThrobberView_throbber_shadowColor,
                DEFAULT_SHADOW_COLOR
        )
        val shadowRadius = arr.getDimension(
                R.styleable.ThrobberView_throbber_shadowRadius,
                densityConverter.dpToPx(DEFAULT_SHADOW_RADIUS_DP)
        )
        val shadowOffsetX = arr.getDimension(
                R.styleable.ThrobberView_throbber_shadowOffsetX,
                densityConverter.dpToPx(DEFAULT_SHADOW_OFFSET_X_DP)
        )
        val shadowOffsetY = arr.getDimension(
                R.styleable.ThrobberView_throbber_shadowOffsetY,
                densityConverter.dpToPx(DEFAULT_SHADOW_OFFSET_Y_DP)
        )
        arr.recycle() // Do this when done.
        if (showCircleBackground) {
            val circleDrawable = CircleDrawable(
                    circleBackgroundColor,
                    densityConverter, shadowColor,
                    densityConverter.pxToDp(shadowRadius),
                    densityConverter.pxToDp(shadowOffsetX),
                    densityConverter.pxToDp(shadowOffsetY)
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                background = circleDrawable
            } else {
                setBackgroundDrawable(circleDrawable)
            }
        }
        basePadding = densityConverter.dpToPx(BASE_PADDING_DP)
        normalizedAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        normalizedAnimator.interpolator = null // Yes, linear interpolator.
        normalizedAnimator.duration = ThrobberMath.DEFAULT_CYCLE_DURATION
        normalizedAnimator.repeatCount = ValueAnimator.INFINITE
        rotationAnimator = ValueAnimator.ofFloat(0.0f, 360.0f)
        rotationAnimator.interpolator = null
        rotationAnimator.duration = ThrobberMath.DEFAULT_ROTATION_CYCLE_DURATION
        rotationAnimator.repeatCount = ValueAnimator.INFINITE
        normalizedAnimator.start()
        rotationAnimator.start()
        invalidate()
    }
}