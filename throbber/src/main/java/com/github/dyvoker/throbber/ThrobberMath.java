package com.github.dyvoker.throbber;

import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Base values and mathematical functions for throbber animation.
 */
public interface ThrobberMath {

	long DEFAULT_CYCLE_DURATION = 1500;
	long DEFAULT_ROTATION_CYCLE_DURATION = 2240;

	AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR =
		new AccelerateDecelerateInterpolator();

	float MIN_SWEEP_ANGLE = 0.05f * 360f; // 5%
	float MAX_ADDITION_SWEEP_ANGLE = 0.70f * 360f; // 70% + 5% = 75%


	/**
	 * @param value Current value of animation from 0.0f to 1.0f.
	 * @return Value of sweep angle for current state of throbber animation.
	 */
	static float calcSweepAngle(float value) {
		// In this case we have two steps of animation:
		// 1. Sweep angle increase;
		// 2. Sweep angle decrease.
		// Both steps will have accelerate-decelerate interpolation!
		if (value < 0.5f) {
			// First step - increased sweep angle.
			// Normalize value from [0.0f; 0.5f] to [0.0f; 1.0f] interval.
			float normalizedValue = value * 2.0f;
			// Now interpolate new value. It needs for accelerate-decelerate effects on start and end.
			float interpolatedValue = ACCELERATE_DECELERATE_INTERPOLATOR.getInterpolation(normalizedValue);
			return MIN_SWEEP_ANGLE + interpolatedValue * MAX_ADDITION_SWEEP_ANGLE;
		}
		// So, here second step - decreased sweep angle.
		// Normalize value from [0.5f; 1.0f] to [1.0f; 0.0f] interval.
		float normalizedValue = Math.abs(value * 2.0f - 2.0f);
		// Now interpolate new value. It needs for accelerate-decelerate effects on start and end.
		float interpolatedValue = ACCELERATE_DECELERATE_INTERPOLATOR.getInterpolation(normalizedValue);
		return MIN_SWEEP_ANGLE + interpolatedValue * MAX_ADDITION_SWEEP_ANGLE;
	}

	/**
	 * @param value Current value of animation from 0.0f to 1.0f.
	 * @return Value of start angle for current state of throbber animation.
	 */
	static float calcStartAngle(float value) {
		if (value < 0.5f) {
			// Only in second part of animation we move start angle.
			return 0.0f;
		}
		// Normalize value from [0.5f; 1.0f] to [0.0f; 1.0f] interval.
		float normalizedValue = value * 2.0f - 1.0f;
		// Now interpolate new value. It needs for accelerate-decelerate effects on start and end.
		float interpolatedValue = ACCELERATE_DECELERATE_INTERPOLATOR.getInterpolation(normalizedValue);
		// Now we can calculate angle. It takes interval [0.0f; 360.0f].
		return interpolatedValue * 360.0f;
	}
}
