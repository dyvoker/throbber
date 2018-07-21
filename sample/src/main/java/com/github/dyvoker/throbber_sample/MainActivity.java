package com.github.dyvoker.throbber_sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.dyvoker.android_utils.DpUtils;
import com.github.dyvoker.throbber.CircleDrawable;
import com.github.dyvoker.throbber.ThrobberView;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Programmatically create throbber.
		ThrobberView throbberView = new ThrobberView(this);
		CircleDrawable circleDrawable =
			new CircleDrawable(Color.WHITE, 0x80000000, 2, 0, 2);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			throbberView.setBackground(circleDrawable);
		} else {
			throbberView.setBackgroundDrawable(circleDrawable);
		}
		FrameLayout throbberContainer = findViewById(R.id.throbber_container);
		throbberContainer.addView(throbberView);
		ViewGroup.LayoutParams layoutParams = throbberView.getLayoutParams();
		layoutParams.width = (int) DpUtils.dpToPx(64);
		layoutParams.height = (int) DpUtils.dpToPx(64);
		throbberView.setLayoutParams(layoutParams);
	}
}
