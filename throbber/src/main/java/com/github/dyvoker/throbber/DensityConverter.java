package com.github.dyvoker.throbber;

import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * Special converter for Density Independence Pixels.
 */
@SuppressWarnings("WeakerAccess")
public class DensityConverter {

	@NonNull private final DisplayMetrics displayMetrics;

	public DensityConverter(@NonNull DisplayMetrics displayMetrics) {
		this.displayMetrics = displayMetrics;
	}

	public float dpToPx(float dp) {
		return dp * displayMetrics.density;
	}

	public float pxToDp(float px) {
		return px / displayMetrics.density;
	}
}
