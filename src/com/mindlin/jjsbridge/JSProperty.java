package com.mindlin.jjsbridge;

import java.io.Serializable;

import org.javatuples.Pair;

public class JSProperty implements Serializable {
	private static final long serialVersionUID = -1993699872660632485L;
	public Object value;
	public boolean enumerable=true;
	public boolean configurable=true;
	public boolean writable=true;
	public WrappedBiFunction<Pair<JSObjectImpl,JSProperty>,Object,Object> setter=null;
	public WrappedFunction<Pair<JSObjectImpl,JSProperty>,Object> getter = null;
	public JSProperty(Object value) {
		this.value=value;
	}
	public JSProperty(Object value, boolean enumerable,
			boolean configurable, boolean writable,
			WrappedFunction<Pair<JSObjectImpl, JSProperty>, Object> getter,
			WrappedBiFunction<Pair<JSObjectImpl, JSProperty>, Object, Object> setter) {
		this.value=value;
		this.enumerable=enumerable;
		this.configurable=configurable;
		this.writable=writable;
		this.getter=getter;
		this.setter=setter;
	}
	public Object get(JSObjectImpl obj) {
		if(getter == null)
			return value;
		return getter.apply(new Pair<JSObjectImpl,JSProperty>(obj,this));
	}
	public void set(JSObjectImpl obj, Object value) {
		if (setter == null)
			this.value = value;
		else
			setter.apply(new Pair<JSObjectImpl, JSProperty>(obj, this), value);
	}
	public String valuestr() {
		return (value instanceof Object)?value.toString():(""+value);
	}
	@Override
	public String toString() {
		return getClass()+"@{value:"+valuestr()+",enumerable:"+enumerable+",configurable:"+configurable+",writable:"+writable+"}";
	}
	public boolean requiresInitialization() {
		return false;
	}
}