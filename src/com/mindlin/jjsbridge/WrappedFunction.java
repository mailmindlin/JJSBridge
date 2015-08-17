package com.mindlin.jjsbridge;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface WrappedFunction<T, R> extends Function<Object[],R>{
	public static <T, R extends Object> WrappedFunction<T,R> forStaticMethodByName(Class<?> clazz, String name) {
		return forStaticMethodByName(clazz,name,(m)->(true));
	}
	public static <T,R extends Object> WrappedFunction<T,R> forMethodByName(Object o, String name) {
		return forMethodByName(o,name,(m)->(true));
	}
	@SuppressWarnings("unchecked")
	public static <T,R extends Object> WrappedFunction<T,R> forStaticMethodByName(Class<?> clazz, String name, Predicate<Method> selector) {
		try {
			final Method m = Arrays.asList(clazz.getDeclaredMethods()).stream().filter(selector).findAny().get();
			return (args)->((R)m.invoke(null,args));
		}catch(NoSuchElementException e) {
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static <T,R extends Object> WrappedFunction<T,R> forMethodByName(Object o, String name, Predicate<Method> selector) {
		try {
			final Method m = Arrays.asList(o.getClass().getDeclaredMethods()).stream().filter(selector).findAny().get();
			return (args)->((R)m.invoke(o,args));
		}catch(NoSuchElementException e) {
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static <T,R extends Object> WrappedFunction<T,R> fromMethod(Method m, Object o) {
		return (args)->((R)m.invoke(o,args));
	}
	@SuppressWarnings("unchecked")
	public static <T,R extends Object> WrappedFunction<T,R> fromStaticMethod(Method m) {
		return (args)->((R)m.invoke(null, args));
	}
	@SuppressWarnings("unchecked")
	public static <T,R extends Object> WrappedFunction<T,R> from(Function<T,R> f) {
		return (Object...a)->(f.apply((T)a[0]));
	}
	@SuppressWarnings("unchecked")
	public static <T,R extends Object> WrappedFunction<T,R> from(ErroneousFunction<T,R> f) {
		return (Object...a)->(f.apply((T)a[0]));
	}
	@Override
	public default R apply(Object...args) throws RuntimeException {
		try {
			return _apply(args);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public R _apply(Object...args) throws Exception;
}
