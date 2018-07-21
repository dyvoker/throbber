package com.github.dyvoker.throbber_sample;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.dyvoker.throbber.CircleDrawable;
import com.github.dyvoker.throbber.ThrobberView;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LinearLayout root = findViewById(R.id.root);

		// Programmatically create throbber.
		ThrobberView throbberView = new ThrobberView(this);
		CircleDrawable circleDrawable =
			new CircleDrawable(Color.WHITE, 0x80000000, 2, 0, 2);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			throbberView.setBackground(circleDrawable);
		} else {
			throbberView.setBackgroundDrawable(circleDrawable);
		}
		root.addView(throbberView);
		ViewGroup.LayoutParams layoutParams = throbberView.getLayoutParams();
		layoutParams.width = (int) dpToPx(64);
		layoutParams.height = (int) dpToPx(64);
		throbberView.setLayoutParams(layoutParams);
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
