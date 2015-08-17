package com.mindlin.jjsbridge.std;

import com.mindlin.jjsbridge.annotations.JSGetter;

public class Navigator {
	@JSGetter("appCodeName")
	public String getAppCodeName() {
		return "JJSBridge";
	}
	@JSGetter("appName")
	public String getAppName() {
		return "JJSBridge";
	}
	@JSGetter("appVersion")
	public String getVersion() {
		return "1.0";
	}
	public final BatteryManager battery = new BatteryManager();
	
}
