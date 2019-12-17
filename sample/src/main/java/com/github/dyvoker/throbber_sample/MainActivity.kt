package com.github.dyvoker.throbber_sample

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.dyvoker.throbber.CircleDrawable
import com.github.dyvoker.throbber.DensityConverter
import com.github.dyvoker.throbber.ThrobberView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val densityConverter = DensityConverter(resources.displayMetrics)
        // Programmatically create throbber.
        val throbberView = ThrobberView(this)
        val circleDrawable = CircleDrawable(
                Color.WHITE,
                densityConverter,
                -0x80000000,
                2f,
                0f,
                2f
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            throbberView.background = circleDrawable
        } else {
            throbberView.setBackgroundDrawable(circleDrawable)
        }
        val throbberContainer = findViewById<FrameLayout>(R.id.throbber_container)
        throbberContainer.addView(throbberView)
        val layoutParams = throbberView.layoutParams
        layoutParams.width = densityConverter.dpToPx(64f).toInt()
        layoutParams.height = densityConverter.dpToPx(64f).toInt()
        throbberView.layoutParams = layoutParams
    }
}