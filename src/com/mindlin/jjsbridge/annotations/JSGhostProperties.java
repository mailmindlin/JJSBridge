package com.mindlin.jjsbridge.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JSGhostProperties {
	JSGhostProperty[] value();
}
