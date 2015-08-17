package com.mindlin.jjsbridge;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.javatuples.Pair;

@FunctionalInterface
public interface WrappedBiFunction<T,U,R> extends WrappedFunction<Pair<T,U>,R>, BiFunction<T, U, R> {
	public static <T,U,R> WrappedBiFunction<T,U,R> from(BiFunction<T,U,R> src) {
		return (T t,U u)->(src.apply(t, u));
	}
	public default R apply(T t, U u) throws RuntimeException {
		try {
			return _apply(t,u);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	default <V> WrappedBiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return null;
    }
	@SuppressWarnings("unchecked")
	@Override
	public default R _apply(Object...args) throws Exception{
		return _bapply((T)args[0],(U)args[1]);
	}
	public R _bapply(T t, U u) throws Exception;
}
