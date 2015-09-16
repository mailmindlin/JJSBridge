package com.mindlin.jjsbridge;

import java.util.function.Predicate;

public interface JSPluginManager extends Predicate<String> {
	Object load(String name);
}
