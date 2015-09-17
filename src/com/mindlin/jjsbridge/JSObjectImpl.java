package com.mindlin.jjsbridge;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import com.mindlin.jjsbridge.JSExternFunctionWrapper.JSExternMethodWrapper;
import com.mindlin.jjsbridge.JSExternFunctionWrapper.JSExternMultiMethodWrapper;
import com.mindlin.jjsbridge.annotations.JSExtern;
import com.mindlin.jjsbridge.annotations.JSPrototype;

import jdk.nashorn.api.scripting.AbstractJSObject;

public class JSObjectImpl extends AbstractJSObject {
	public static Method[] getExternalizedMethods(Class<?> c) {
		return null;//TODO fin
	}
	@JSPrototype
	protected ConcurrentHashMap<String, JSProperty> properties = new ConcurrentHashMap<String, JSProperty>();
	public JSObjectImpl() {
		Class<?> clazz=getClass();
		//load fields
		Arrays.asList(clazz.getDeclaredFields())
				.stream()
				.filter((f)->(f.isAnnotationPresent(JSExtern.class)))
				.forEach((f)->{
					JSExtern definition = f.getAnnotation(JSExtern.class);
					try {
						String name=definition.name().isEmpty()?f.getName():definition.name();
						JSProperty prop=new JSProperty(
								f.get(this),
								definition.enumerable(),
								definition.configurable(),
								definition.writable(),
								WrappedFunction.from((p)->(f.get(p.getValue0()))),
								null);
						properties.put(name, prop);
						System.out.println("Creating property '"+name+"': "+prop+ " from " + f);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		//load methods
		Arrays.asList(clazz.getDeclaredMethods())
			.stream()
			.filter((f)->(f.isAnnotationPresent(JSExtern.class)))
			.forEach((m)->{
			JSExtern definition = m.getAnnotation(JSExtern.class);
			try {
				String name=definition.name().isEmpty()?m.getName():definition.name();
				final JSProperty prop=properties.getOrDefault(name, new JSProperty(null,//value is null because it will be set later
					definition.enumerable(),
					definition.configurable(),
					definition.writable(),
					null,
					null));
				if(prop.value instanceof JSExternMethodWrapper)
					//overloading support
					prop.value=JSExternFunctionWrapper.fromMethods(m,((JSExternMethodWrapper<?>)properties.get(name).value).m.get());
				else if(prop.value instanceof JSExternMultiMethodWrapper)
					((JSExternMultiMethodWrapper<?>)prop.value).add(m);
				else
					prop.value=JSExternFunctionWrapper.fromMethod(m);
				properties.put(name, prop);
				System.out.println("Externalized method '"+name+"': "+prop+ " from " + m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	@Override
	public Object getMember(String name) {
		System.out.println("Getting member: "+name);
		return __pubget(name);
	}
	@Override
	public Object getSlot(int index) {
		return properties.get(""+index).value;
	}
	@Override
	public boolean hasMember(String name) {
		System.out.println("Checking for member "+name);
		return properties.containsKey(name);
	}
	@Override
	public boolean hasSlot(int slot) {
		return properties.containsKey(""+slot);
	}
	@Override
	public boolean isFunction() {
		return true;
	}
	@Override
	public boolean isStrictFunction() {
		return true;
	}
	@Override
	public Set<String> keySet() {
		return properties.entrySet().stream().filter((entry)->(entry.getValue().enumerable)).map((entry)->(entry.getKey())).collect(Collectors.toCollection(()->(new HashSet<String>())));
	}
	@Override
	public Object newObject(Object... args) {
		System.out.println("New object: "+Arrays.toString(args));
		return null;
	}
	@Override
	public void removeMember(String name) {
		if(!properties.containsKey(name))
			return;
		JSProperty prop=properties.get(name);
		if(prop.configurable)
			properties.remove(name);
	}
	@Override
	public void setMember(String name, Object value) {
		System.out.println("Setting member "+ name + " to " + value);
		__pubset(name,value);
	}
	@Override
	public void setSlot(int index, Object value) {
		__pubset(""+index,value);
	}
	@Override
	public double toNumber() {
		return Double.NaN;
	}
	@Override
	public Collection<Object> values() {
		//return all the enumerable values
		return properties.values().stream().filter((prop)->(prop.enumerable)).collect(Collectors.toCollection(()->(new ArrayList<Object>())));
	}
	protected void __pubset(String key, Object value) {
		if(!properties.containsKey(key))
			properties.put(key, new JSProperty(value));
		else {
			JSProperty prop = properties.get(key);
			if(prop.writable) {
				if(prop.setter!=null)
					prop.value=prop.setter.apply(new Pair<JSObjectImpl,JSProperty>(this,prop),value);
				else
					prop.value=value;
			} else
				System.out.println("Not writable.");
		}
	}
	protected Object __pubget(String key) {
		if(!properties.containsKey(key))
			return null;
		else
			return properties.get(key).get(this);
	}
	protected void defineProperty(String name, Object value, boolean enumerable, boolean configurable, boolean writable, WrappedFunction<Pair<JSObjectImpl,JSProperty>,Object> getter, WrappedBiFunction<Pair<JSObjectImpl,JSProperty>,Object,Object> setter) {
		properties.put(name, new JSProperty(value,enumerable,configurable,writable,getter,setter));
	}
	protected void __protset(String key, Object value) {
		if(!properties.containsKey(key))
			properties.put(key, new JSProperty(value));
		properties.get(key).value=value;
	}
	//std. Object methods
	@JSExtern(readable=true,writable=true,configurable=false)
	public boolean hasOwnProperty(String prop) {
		//TODO stub
		Arrays.asList(getClass().getDeclaredMethods()).stream();
		return true;
	}
}
