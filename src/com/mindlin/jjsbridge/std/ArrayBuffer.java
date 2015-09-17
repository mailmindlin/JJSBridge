package com.mindlin.jjsbridge.std;

import java.nio.ByteBuffer;
import java.util.HashMap;

import com.mindlin.jjsbridge.JSObjectImpl;
import com.mindlin.jjsbridge.JSProperty;
import com.mindlin.jjsbridge.annotations.JSPrototype;

public class ArrayBuffer {
	//NOT externalized
	protected final ByteBuffer buffer;
	@JSPrototype
	protected static final HashMap<String, JSProperty> prototype = JSObjectImpl.__proto__(ArrayBuffer.class);
	public ArrayBuffer(int length) {
		buffer = ByteBuffer.allocate(length);//TODO should be allocateDirect?
	}
}
