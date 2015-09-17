package com.mindlin.jjsbridge;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;

import com.mindlin.jjsbridge.annotations.JSExtern;
import com.mindlin.jjsbridge.annotations.JSGetter;
import com.mindlin.jjsbridge.annotations.JSGhostProperty;
import com.mindlin.jjsbridge.annotations.JSSetter;

public class PrototypeGenerator {
	public static HashMap<String, JSProperty> apply(Class<?> c) {
		HashMap<String, JSProperty> result = new HashMap<>();
		//get ghost methods
		for (JSGhostProperty ghostProperty : c.getAnnotationsByType(JSGhostProperty.class)) {
			JSProperty proto = new JSProperty(null, ghostProperty.enumerable(),ghostProperty.configurable(), ghostProperty.writable(), null, null);
			result.put(ghostProperty.name(), proto);
		}
		
		for(Field field : c.getDeclaredFields()) {
			if(field.isAnnotationPresent(JSExtern.class)) {
				JSExtern info = field.getAnnotation(JSExtern.class);
				String name = info.name();
				if (name.isEmpty())
					name = field.getName();
				field.setAccessible(true);
				Object value = null;
				try {
					value = ReflectionUtils.get(field, null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				result.put(name, new JSProperty(value, info.enumerable(), info.configurable(), info.writable(), null, null));
			}
		}
		LinkedList<Method> externs = new LinkedList<>(),
				getters = new LinkedList<>(),
				setters = new LinkedList<>();
		for(Method method : ReflectionUtils.join(c.getDeclaredMethods(), c.getMethods())) {
			if (method.isAnnotationPresent(JSExtern.class))
				externs.add(method);
			if (method.isAnnotationPresent(JSGetter.class))
				externs.add(method);
			if (method.isAnnotationPresent(JSSetter.class));
		}
		for (Method extern : externs) {
			JSExtern info = extern.getAnnotation(JSExtern.class);
			String name = info.name();
			if(name.isEmpty())
				name = extern.getName();
			//TODO get value
			result.put(name, new JSProperty(null, info.enumerable(), info.configurable(), info.writable(), null, null));
		}
		for (Method getter : getters) {
			JSGetter info = getter.getAnnotation(JSGetter.class);
			String name = info.value();
			if (name.isEmpty()) {
				String methodName = getter.getName();
				if (methodName.startsWith("get") & methodName.length()>3)
					name = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
				else
					throw new RuntimeException("No value specified for getter");
			}
			if (!result.containsKey(name))
				throw new RuntimeException("No field or property for getter '"+name+"'. Try declaring it at the class level with @JSGhostProperty");
				
				JSProperty prop = result.get(name);
			
				if ((getter.getModifiers() & Modifier.STATIC) >0)
		}
		for (Method setter : setters) {
			JSSetter info = setter.getAnnotation(JSSetter.class);
			
			String name = info.value();
			
			if (name.isEmpty()) {
				String methodName = setter.getName();
				if (methodName.startsWith("get") & methodName.length()>3)
					name = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
				else
					throw new RuntimeException("No value specified for getter");
			}
			
			if (!result.containsKey(name))
				throw new RuntimeException("No field or property for setter '"+name+"'. Try declaring it at the class level with @JSGhostProperty");
			
			JSProperty prop = result.get(name);
		}
		return result;
	}
}
