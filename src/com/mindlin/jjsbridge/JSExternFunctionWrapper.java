package com.mindlin.jjsbridge;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JSExternFunctionWrapper<R> extends JSObjectImpl {
	public static <R extends Object> JSExternFunctionWrapper<R> fromMethod(Method m) {
		return new JSExternMethodWrapper<R>(m);
	}

	public static <R> JSExternFunctionWrapper<R> fromMethods(Method...methods) {
		return new JSExternMultiMethodWrapper<R>(methods);
	}

	protected WrappedBiFunction<Object, Object[], R> f;

	public JSExternFunctionWrapper(WrappedBiFunction<Object, Object[], R> f) {
		super();
		this.f = f;
	}

	@Override
	public R call(final Object thiz, final Object... args) {
		return f.apply(thiz, args);
	}
	public static class JSExternMethodWrapper<R> extends JSExternFunctionWrapper<R> {
		protected final WeakReference<Method> m;
		@SuppressWarnings("unchecked")
		public JSExternMethodWrapper(Method m) {
			super((thiz, _args) -> {
				System.out.println("Args ("+_args.length+"): "+Arrays.toString(_args));
				Object[] args = new Object[_args.length];
				Class<?>[] types = m.getParameterTypes();
				for (int i = 0; i < _args.length; ++i)
					args[i] = ReflectionUtils.resolve(_args[i],
							types[i]);
				return (R) m.invoke(thiz, args);
			});
			this.m=new WeakReference<Method>(m);
		}
	}
	public static class JSExternMultiMethodWrapper<R> extends JSExternFunctionWrapper<R> implements Set<Method> {
		public final HashSet<Method> methods = new HashSet<Method>();
		public JSExternMultiMethodWrapper(Method...m) {
			super(null);
			this.methods.addAll(Arrays.asList(m));
			this.f=this::apply;
		}
		@SuppressWarnings("unchecked")
		public R apply(Object thiz, final Object[] _args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				Iterator<Method> i = methods.iterator();
				while(i.hasNext()) {
					Method m=i.next();
					Class<?>[] types=m.getParameterTypes();
					if(types.length!=_args.length)
						continue;
					Object[] args = new Object[_args.length];
					try {
						for (int j = 0; j < _args.length; j++)
							args[j] = ReflectionUtils.resolve(_args[j], types[j]);
					}catch(Exception e){continue;}
					return (R) m.invoke(thiz, args);
				}
				StringBuffer sb = new StringBuffer("Illegal arguments (")
					.append(_args.length)
					.append(") :")
					.append(Arrays.toString(Arrays.asList(_args)
						.stream()
						.map((arg)->(arg.getClass().getCanonicalName()))
						.toArray()));
				for(Method m : methods) {
					sb.append("\n\tTested method: ").append(m.toString());
				}
				if(methods.size()==0)
					sb.append("\n\tNo methods?");
				throw new IllegalArgumentException(sb.toString());
		}
		@Override
		public int size() {
			return methods.size();
		}

		@Override
		public boolean isEmpty() {
			return methods.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return methods.contains(o);
		}

		@Override
		public Iterator<Method> iterator() {
			return methods.iterator();
		}

		@Override
		public Object[] toArray() {
			return methods.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return methods.toArray(a);
		}

		@Override
		public boolean add(Method e) {
			return methods.add(e);
		}
		
		public boolean add(JSExternMethodWrapper<?> jemr) {
			return methods.add(jemr.m.get());
		}

		@Override
		public boolean remove(Object o) {
			return methods.remove(o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return methods.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends Method> c) {
			return methods.addAll(c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return methods.removeAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return methods.retainAll(c);
		}

		@Override
		public void clear() {
			methods.clear();
		}
		
	}
}
