package com.example.glessandbox;

public class NDKSandbox {
	static {
		System.loadLibrary("NDKSandbox");
	}
	public static native void update(int program);
}
