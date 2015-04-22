package com.sensolabs.tabs.databases;

/*
 * 
 * <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.VIBRATE" />
 */
/**
 * 
 Is a getter and a setter Java class where setter are used to store the data from user in Database through EditText fields.
 Whereas getter are used for retriving the stored data from Database. 
 */
public class Patient {
	private String id;
	private String firstName;
	private String name;
	private String numHeartRateMin, numHeartRateMax, numInterBeatIntervalMin, numInterBeatIntervalMax,
	                 numBreathingrateMin, numBreathingrateMax;
	
	private boolean Alarm,Message;
//	private String BRAlarm,BRMessage;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumHeartRateMin() {
		return numHeartRateMin;
	}
	public void setNumHeartRateMin(String numHeartRateMin) {
		this.numHeartRateMin = numHeartRateMin;
	}
	public String getNumHeartRateMax() {
		return numHeartRateMax;
	}
	public void setNumHeartRateMax(String numHeartRateMax) {
		this.numHeartRateMax = numHeartRateMax;
	}
	public String getNumInterBeatIntervalMin() {
		return numInterBeatIntervalMin;
	}
	public void setNumInterBeatIntervalMin(String numInterBeatIntervalMin) {
		this.numInterBeatIntervalMin = numInterBeatIntervalMin;
	}
	public String getNumInterBeatIntervalMax() {
		return numInterBeatIntervalMax;
	}
	public void setNumInterBeatIntervalMax(String numInterBeatIntervalMax) {
		this.numInterBeatIntervalMax = numInterBeatIntervalMax;
	}
	public String getNumBreathingrateMin() {
		return numBreathingrateMin;
	}
	public void setNumBreathingrateMin(String numBreathingrateMin) {
		this.numBreathingrateMin = numBreathingrateMin;
	}
	public String getNumBreathingrateMax() {
		return numBreathingrateMax;
	}
	public void setNumBreathingrateMax(String numBreathingrateMax) {
		this.numBreathingrateMax = numBreathingrateMax;
	}
	//CheckBox for Heart
	public boolean getAlarm() {
		return Alarm;
	}
	public void setAlarm(boolean Alarm  ) {
		this.Alarm = Alarm;
	}
	public boolean getMessage() {
		return Message;
	}
	public void setMessage(boolean Message) {
		this.Message = Message;
	}

}
