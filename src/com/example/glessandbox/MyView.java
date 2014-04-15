package com.example.glessandbox;

import android.opengl.GLSurfaceView;
import android.content.Context;
import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyView extends GLSurfaceView {
	public MyView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(new MyRenderer());
	}

	private class MyRenderer implements GLSurfaceView.Renderer {
		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			GLES20.glClearColor(0, 0, 0, 1);
		}

		public void onDrawFrame(GL10 unused) {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		}

		public void onSurfaceChanged(GL10 unused, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
		}
	}
}
