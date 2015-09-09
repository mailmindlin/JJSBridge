package com.mindlin.jjsbridge.std;

public class Console {
	public void log(String s) {
		System.out.println("JS::log: "+s);
	}
	public void warn(String s) {
		System.out.println("JS::warn: "+s);
	}
	public void error(String s) {
		System.out.println("JS::error: "+s);
	}
}
