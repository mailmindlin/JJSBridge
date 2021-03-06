package com.mindlin.jjsbridge;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ReflectionUtils {
	public static Object box(Object prim) {
		Class<?> pcl=prim.getClass();
		if(pcl.equals(byte.class))
			return new Byte((byte)prim);
		else if(pcl.equals(short.class))
			return new Short((short)prim);
		else if(pcl.equals(int.class))
			return new Integer((int)prim);
		else if(pcl.equals(long.class))
			return new Long((int)prim);
		else if(pcl.equals(float.class))
			return new Float((float)prim);
		else if(pcl.equals(double.class))
			return new Double((double)prim);
		else if(pcl.equals(boolean.class))
			return new Boolean((boolean)prim);
		else if(pcl.equals(char.class))
			return new Character((char)prim);
		else
			return prim;
	}
	/**
	 * Convert class to boxed version of said class
	 * @param boxed
	 * @return
	 */
	public static Class<?> boxClass(Class<?> boxed) {
		if(boxed.equals(byte.class))
			return Byte.class;
		else if(boxed.equals(short.class))
			return Short.class;
		else if(boxed.equals(int.class))
			return Integer.class;
		else if(boxed.equals(long.class))
			return Long.class;
		else if(boxed.equals(float.class))
			return Float.class;
		else if(boxed.equals(double.class))
			return Double.class;
		else if(boxed.equals(boolean.class))
			return Boolean.class;
		else if(boxed.equals(char.class))
			return Character.class;
		else
			throw new IllegalArgumentException("Illegal class "+boxed.toString());
	}
	public static Object unbox(Object boxed) {
		Class<?> bcl=boxed.getClass();
		if(bcl.equals(Byte.class))
			return (byte)boxed;
		else if(bcl.equals(Short.class))
			return (short)boxed;
		else if(bcl.equals(Integer.class))
			return (int)boxed;
		else if(bcl.equals(Long.class))
			return (long)boxed;
		else if(bcl.equals(Float.class))
			return (float)boxed;
		else if(bcl.equals(Double.class))
			return (double)boxed;
		else if(bcl.equals(Boolean.class))
			return (boolean)boxed;
		else if(bcl.equals(Character.class))
			return (char)boxed;
		else
			return boxed;
	}
	/**
	 * Converts number to 
	 * @param n
	 * @param unboxed
	 * @return
	 */
	public static Object convertNumber(Number n, Class<?> unboxed) {
		if(unboxed.equals(byte.class))
			return n.byteValue();
		else if(unboxed.equals(short.class))
			return n.shortValue();
		else if(unboxed.equals(int.class))
			return n.intValue();
		else if(unboxed.equals(long.class))
			return n.longValue();
		else if(unboxed.equals(float.class))
			return n.floatValue();
		else if(unboxed.equals(double.class))
			return n.doubleValue();
		else
			return null;
	}
	/**
	 * Tries to cast object a to class B, trying many different methods.
	 * @param a
	 * @param bcl
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <A, B> B resolve(A a, Class<B> bcl) {
		if(a==null)
			return null;
		try {
			return bcl.cast(a);
		} catch (ClassCastException cce) {}
		if (bcl.equals(String.class))// check if bcl is String
			return (B) a.toString();
		//fixes for boxed/unboxed primitives
		Class<?> acl=a.getClass();
		Object abox=box(a);
		Class<?> bclbox=boxClass(bcl);
		if(abox.getClass().equals(bcl))
			return (B)abox;
		if(acl.equals(bclbox))
			return (B)unbox(a);
		throw new ClassCastException("Can't figure out how to cast "
				+ a.getClass().getName() + " to " + bcl.getName());
	}
	public static Object get(Field f, Object donor) throws IllegalArgumentException, IllegalAccessException {
		boolean wasAccessible = f.isAccessible();
		try {
			f.setAccessible(true);
			return f.get(donor);
		} finally {
			f.setAccessible(wasAccessible);
		}
	}
	@SuppressWarnings("unchecked")
	public static <A> Iterable<A> join(A[]...args) {
		int len = 0;
		Set<A> result = new HashSet<A>();
		for(A[] arg : args)
			result.addAll(Arrays.asList(arg));
		return result;
	}
}
