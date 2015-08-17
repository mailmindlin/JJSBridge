package com.mindlin.jjsbridge;

/**
 * ClassLoader that only allows the loading of 
 * @author mailmindlin
 */
public class JSSandboxedClassLoader extends ClassLoader {
	public JSSandboxedClassLoader() {
		
	}
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		throw new ClassNotFoundException("Haha...no "+name+" for you.");
    }
}
