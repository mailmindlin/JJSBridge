package com.mindlin.jjsbridge.annotations;

import java.lang.annotation.Repeatable;

/**
 * Way to declare a virtual property (only supported by getters/setters).
 * @author mailmindlin
 *
 */
@Repeatable(JSGhostProperties.class)
public @interface JSGhostProperty {
	/**
	 * Name
	 * @return
	 */
	String name();
	boolean visible() default true;
	boolean readable() default true;
	boolean writable() default true;
	boolean configurable() default true;
	boolean enumerable() default true;
	boolean prototypic() default true;//TODO figure out what that is
}
