package com.mindlin.jjsbridge.std;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mindlin.jjsbridge.annotations.JSGetter;
import com.mindlin.jjsbridge.annotations.JSGhostProperty;

//TODO finish (see http://stackoverflow.com/questions/3434719/how-to-get-the-remaining-battery-life-in-a-windows-system)
@JSGhostProperty(name="charging",readable=true,writable=false,configurable=false,enumerable=true,prototypic=false)
@JSGhostProperty(name="chargingTime",readable=true,writable=false,configurable=false,enumerable=true,prototypic=false)
@JSGhostProperty(name="dischargingTime",readable=true,writable=false,configurable=false,enumerable=true,prototypic=false)
@JSGhostProperty(name="level",readable=true,writable=false,configurable=false,enumerable=true,prototypic=false)
public class BatteryManager {
	public BatteryManager() {
		
	}
	//TODO implement for windows
	protected int[] getMacBatteryLevel() {
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"pmset","-g","ps"});
			p.waitFor();
			try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				br.readLine();
				String line = br.readLine();
				line = line.substring(line.indexOf('\t')+1);
				String[] data = line.split(";");
				int[] result = new int[3];
				result[0] = Integer.parseInt(data[0].substring(0,data[0].indexOf('%')));
				result[1] = (data[1].endsWith("discharging")?1:(data[1].endsWith("charging")?2:0));
				if(data[2].endsWith("remaining")) {
					data[2]=data[2].trim();
					data[2]=data[2].substring(0,data[2].indexOf(' '));
					result[2] = (Integer.parseInt(data[2].substring(0,data[2].indexOf(':')))*3600) + (Integer.parseInt(data[2].substring(data[2].indexOf(':')+1))*60);
				} else {
					result[2]=-1;
				}
				return result;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	@JSGetter("charging")
	public boolean isCharging() {
		int[] tmp = getMacBatteryLevel();
		if(tmp!=null)
			return tmp[1]==2;
		return false;
	}
	@JSGetter("chargingTime")
	public int getChargingTime() {
		int[] tmp = getMacBatteryLevel();
		if(tmp!=null)
			return tmp[2];
		return -1;
	}
	@JSGetter("dischargingTime")
	public int getDischargingTime() {
		int[] tmp = getMacBatteryLevel();
		if(tmp!=null)
			return tmp[2];
		return -1;
	}
	@JSGetter("level")
	public double getBatteryLevel() {
		int[] tmp = getMacBatteryLevel();
		if(tmp!=null)
			return ((double)tmp[0])/100.0;
		return Double.POSITIVE_INFINITY;
	}
}
