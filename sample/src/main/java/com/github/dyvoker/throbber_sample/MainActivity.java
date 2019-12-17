package com.github.dyvoker.throbber_sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.github.dyvoker.throbber.CircleDrawable;
import com.github.dyvoker.throbber.DensityConverter;
import com.github.dyvoker.throbber.ThrobberView;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DensityConverter densityConverter = new DensityConverter(getResources().getDisplayMetrics());

		// Programmatically create throbber.
		ThrobberView throbberView = new ThrobberView(this);
		CircleDrawable circleDrawable = new CircleDrawable(
			Color.WHITE,
			densityConverter,
			0x80000000,
			2,
			0,
			2
		);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			throbberView.setBackground(circleDrawable);
		} else {
			throbberView.setBackgroundDrawable(circleDrawable);
		}
		FrameLayout throbberContainer = findViewById(R.id.throbber_container);
		throbberContainer.addView(throbberView);
		ViewGroup.LayoutParams layoutParams = throbberView.getLayoutParams();
		layoutParams.width = (int) densityConverter.dpToPx(64);
		layoutParams.height = (int) densityConverter.dpToPx(64);
		throbberView.setLayoutParams(layoutParams);
	}
}
