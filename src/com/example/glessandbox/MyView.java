package com.example.glessandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	static Context context;

	public MyView(Context c) {
		super(c);
		context = c;
		setEGLContextClientVersion(2);
		setRenderer(new MyRenderer());
	}

	public static String load(String fileName) {
		AssetManager assetManager = context.getAssets();
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

	public static int loadTexture(String s){
		Bitmap img;
		try {
			img = BitmapFactory.decodeStream(context.getAssets().open(s));
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

	public static int compileShader(int type, String fileName) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, MyView.load(fileName));
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

	public static int createProgram(String name) {
		int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, "shaders/" + name + ".vs");
		int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, "shaders/" + name + ".fs");
		int program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);
		GLES20.glLinkProgram(program);
		return program;
	}

	private class MyRenderer implements GLSurfaceView.Renderer {
		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			GLES20.glClearColor(0, 0, 0, 1);

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			MyView.loadTexture("rose.jpg");
			GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
			MyView.loadTexture("autumn.jpg");

			NDKSandbox.init();
		}

		public void onDrawFrame(GL10 unused) {
			NDKSandbox.update();
		}

		public void onSurfaceChanged(GL10 unused, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
		}
	}
}
