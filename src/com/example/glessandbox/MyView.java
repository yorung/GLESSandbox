package com.example.glessandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLSurfaceView;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyView extends GLSurfaceView {
	public MyView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(new MyRenderer(this));
	}

	public String load(String fileName) {
		AssetManager assetManager = getResources().getAssets();
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = assetManager.open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String tmp;
			while((tmp = br.readLine()) != null) {
				sb.append(tmp + "\n");
			}
			br.close();
		} catch (IOException e) {
		}
		return sb.toString();
	}

	private static final float triangleCoords[] = {
		0.0f,  0.5f,
		-0.8f, -0.6f,
		0.8f, -0.6f,
	};

	private class MyRenderer implements GLSurfaceView.Renderer {
		private MyView view;
		private int mProgram;
		private FloatBuffer vertexBuffer;

		private static final int COORDS_PER_VERTEX = 2;
		private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
		private final int vertexStride = COORDS_PER_VERTEX * 4;

		public MyRenderer(MyView v) {
			view = v;
			ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(triangleCoords);
			vertexBuffer.position(0);
		}

		private int compileShader(int type, String fileName) {
			int shader = GLES20.glCreateShader(type);
			GLES20.glShaderSource(shader, view.load(fileName));
			GLES20.glCompileShader(shader);
			return shader;
		}

		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			GLES20.glClearColor(0, 0, 0, 1);
			int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, "vs.vs");
			int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, "fs.fs");
			mProgram = GLES20.glCreateProgram();
			GLES20.glAttachShader(mProgram, vertexShader);
			GLES20.glAttachShader(mProgram, fragmentShader);
			GLES20.glLinkProgram(mProgram);
		}

		public void onDrawFrame(GL10 unused) {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
			GLES20.glUseProgram(mProgram);
			int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}

		public void onSurfaceChanged(GL10 unused, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
		}
	}
}
