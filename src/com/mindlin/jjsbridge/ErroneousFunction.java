package com.mindlin.jjsbridge;

import java.util.function.Function;

/**
 * Basically a function that's allowed to throw an exception. It passes RuntimeExceptions up through {@link Function#apply(Object)},
 * and wrapps any other throwables in a RuntimeException. All methods from {@link Function} should apply.
 * @author mailmindlin
 *
 * @param <T> the type of input to the function
 * @param <R> the type of result of the function
 * @see Function
 */
@FunctionalInterface
public interface ErroneousFunction<T, R> extends Function<T, R> {
	@Override
	default R apply(T t) {
		try {
			return _apply(t);
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * @param t
	 * @return result
	 * @throws Throwable
	 */
	R _apply(T t) throws Throwable;
}
