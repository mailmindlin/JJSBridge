package com.mindlin.jjsbridge;

public @interface JSExtern {
	/**
	 * Not empty iff the name 
	String name default "";
	/**
	 * {@code true} iff the type of this property descriptor may be changed and if the property may be deleted from the corresponding object.
	 */
	boolean configurable() default false;
	/**
	 * True iff this property shows up during the enumeration of the properties on the corresponding object.
	 */
	boolean enumerable() default false;
	/**
	 * True iff the value associated with the porperty may be changed with an assignment operator.
	 * Must be false for final fields (setting other wise will either throw an error or be ignored)
	 */
	boolean writable() default false;
}
