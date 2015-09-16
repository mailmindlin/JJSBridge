package com.mindlin.jjsbridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.script.CompiledScript;
import javax.script.ScriptException;

import com.mindlin.jjsbridge.std.Console;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class JSBridge {
	protected JSSandboxedClassLoader classLoader;
	protected final NashornScriptEngine engine;
	protected final List<JSPluginManager> pluginManagers = new ArrayList<>();
	public JSBridge() {
		engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine(classLoader);
		engine.put("require", ((Function<String, Object>)(name)->{
			Optional<JSPluginManager> manager = pluginManagers.stream().filter((mgr)->(mgr.test(name))).findFirst();
			if (!manager.isPresent())
				return null;
			return manager.get().load(name);
		}));
	}
	public JSBridge expose(String name) {
		switch(name) {
		case "console":
			engine.put("console", new Console());
			break;
		case "ArrayBuffer":
//			engine.put("ArrayBuffer");
		}
		return this;
	}
	public JSBridge exposeClass(String className, String name) throws ScriptException {
		engine.put(name, engine.eval(className));
		return this;
	}
	public CompiledScript compile(BufferedReader js) throws ScriptException {
		return engine.compile(js);
	}
	public void eval(String s) throws ScriptException {
		engine.eval(s);
	}
	public void eval(BufferedReader br) throws ScriptException {
		engine.eval(br);
	}
	public void exposeObject(String name, JSObject object) {
		engine.put(name, object);
	}
	public void exposeObject(String name, Object object) {
		
	}
	public Object invokeFunction(String name, Object...args) throws NoSuchMethodException, ScriptException {
		return engine.invokeFunction(name, args);
	}
	public void compile(File f) throws ScriptException, IOException {
		compile(f.toPath());
	}
	public void compile(Path p) throws ScriptException, IOException {
		compile(Files.newBufferedReader(p));
	}
	public void compile(URI uri) throws IOException, ScriptException {
		try {
			URL url = uri.toURL();
			compile(new BufferedReader(new InputStreamReader(url.openStream())));
		}catch (MalformedURLException e) {
			throw new IOException(e);
		}
	}
}
