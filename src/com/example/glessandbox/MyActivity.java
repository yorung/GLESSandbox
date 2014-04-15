package com.example.glessandbox;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MyActivity extends Activity {
	private GLSurfaceView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new MyView(getApplication());
		setContentView(view);
	}

	@Override
	public void onPause() {
		super.onPause();
		view.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		view.onResume();
	}
};
