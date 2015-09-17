package com.mindlin.jjsbridge.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a function as a property setter. Note that the field does not actually have to be declared in the
 * Java code, as they can be virtually generated.<br/>
 * <h3>Virtual Fields</h3>
 * If you using this to generate a virtual field from a setter, then you should also annotate the class
 * with {@link JSExtern} to declare the field's properties. Note that JSExtern only has to/can be used
 * once per field, or one of them will be used at random. This also means that if you use it for 
 * @author mailmindlin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JSSetter {
	/**
	 * Name of property to modify (full name, must be in class).<br/>
	 * i.e., {@code com.example.Foo#bar} would just be {@code bar}, but must be declared
	 * in class {@code com.example.Foo}.<br/>
	 * <small>(sorry that it's kinda hard to explain)</small>
	 */
	public String value() default "";
}