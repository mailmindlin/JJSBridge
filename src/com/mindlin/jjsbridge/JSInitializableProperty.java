package com.mindlin.jjsbridge;

import org.javatuples.Pair;

class JSInitializableProperty extends JSProperty {
	private static final long serialVersionUID = -834637759078788495L;

	public JSInitializableProperty(Object value) {
		super(value);
	}
	public JSInitializableProperty(Object value, boolean enumerable,
			boolean configurable, boolean writable,
			WrappedFunction<Pair<JSObjectImpl, JSProperty>, Object> getter,
			WrappedBiFunction<Pair<JSObjectImpl, JSProperty>, Object, Object> setter) {
		super(value, enumerable, configurable, writable, getter, setter);
	}
	
	@Override
	public boolean requiresInitialization() {
		return true;
	}

}
