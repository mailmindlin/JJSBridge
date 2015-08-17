package com.mindlin.jjsbridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.script.CompiledScript;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class JSBridge {
	protected JSSandboxedClassLoader classLoader;
	protected final NashornScriptEngine engine;
	public JSBridge() {
		engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine(classLoader);
		try {
			engine.put("console", java.lang.System.out);
			engine.eval("load('nashorn:mozilla_compat.js');");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
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
