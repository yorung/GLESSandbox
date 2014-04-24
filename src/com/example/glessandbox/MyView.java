package com.example.glessandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

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

	public int loadTexture(String s){
		Bitmap img;
		try {
			img = BitmapFactory.decodeStream(getContext().getAssets().open(s));
		} catch (IOException e) {
			return -1;
		}
		int tex[] = new int[1];
		GLES20.glGenTextures(1, tex, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, img, 0);
		img.recycle();
		return tex[0];
	}

	private static final float triangleCoords[] = {
		-1, -1,
		1, -1,
		-1, 1,
		1, 1,
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
			int result[] = new int[1];
			GLES20.glCompileShader(shader);
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, result, 0);
			if (result[0] == 0) {
				String errStr = GLES20.glGetShaderInfoLog(shader);
				String errStr2 = String.format("result=%d log=%s", result[0], errStr);
				Log.e("MyView", errStr2);
			}
			return shader;
		}

		private int createProgram(String name) {
			int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, "shaders/" + name + ".vs");
			int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, "shaders/" + name + ".fs");
			int program = GLES20.glCreateProgram();
			GLES20.glAttachShader(program, vertexShader);
			GLES20.glAttachShader(program, fragmentShader);
			GLES20.glLinkProgram(program);
			return program;
		}

		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			GLES20.glClearColor(0, 0, 0, 1);

//			mProgram = createProgram("water");
			mProgram = createProgram("vivid");

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			view.loadTexture("rose.jpg");
			GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
			view.loadTexture("autumn.jpg");
		}

		int frame = 0;
		public void onDrawFrame(GL10 unused) {
			frame++;

			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
			GLES20.glUseProgram(mProgram);
			GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "sampler1"), 0);
			GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "sampler2"), 1);
			GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "time"), (float)frame / 60.0f);
			int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}

		public void onSurfaceChanged(GL10 unused, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
		}
	}
}
