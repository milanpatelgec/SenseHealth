package com.sensolabs.tabs.classes.object;

public class JPatient {
	
	public String getP_id() {
		return p_id;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getArrived() {
		return arrived;
	}
	public void setArrived(String arrived) {
		this.arrived = arrived;
	}
	public String getECG() {
		return ECG;
	}
	public void setECG(String eCG) {
		ECG = eCG;
	}
	private String p_id;
	private String fname;
	private String lname;
	private String arrived;
	private String ECG;

}
