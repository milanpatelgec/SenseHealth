package com.sensolabs.tabs.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class Persistenses {
	
	private static final String PREF = "PREF_SENSO_LABS";
	private static final String PREF_DEVICE_NAME = "PREF_DEVICES_NAME";
	private static final String PREF_DEVICE_Address = "PREF_DEVICES_ADDRESS";
	
	private SharedPreferences settings;

	public Persistenses(Context context) {
		settings = context.getSharedPreferences(PREF, 0);
	}
	
	public void putDeviceName(String deviceName) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREF_DEVICE_NAME, deviceName);
		editor.commit();
	}
	
	public String getDeviceName() {
		return settings.getString(PREF_DEVICE_NAME, null);
	}
	
	public void putDeviceAddress(String DeviceAddress) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREF_DEVICE_Address, DeviceAddress);
		editor.commit();
	}
	
	public String getDeviceAddress() {
		return settings.getString(PREF_DEVICE_Address, null);
	}

}
