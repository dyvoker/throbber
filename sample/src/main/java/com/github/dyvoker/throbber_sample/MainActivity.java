package com.github.dyvoker.throbber_sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.dyvoker.throbber.ThrobberView;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LinearLayout root = findViewById(R.id.root);

		ThrobberView throbberView = new ThrobberView(this);

		root.addView(throbberView);
	}
}
