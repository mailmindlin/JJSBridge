package com.mindlin.jjsbridge.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JSExtern {
	String name() default "";
	boolean visible() default true;
	boolean readable() default true;
	boolean writable() default true;
	boolean configurable() default true;
	boolean enumerable() default true;
	boolean prototypic() default false;
}
