package com.example.glessandbox;

public class NDKSandbox {
	static {
		System.loadLibrary("NDKSandbox");
	}
	public static native void init();
	public static native void update();
}
