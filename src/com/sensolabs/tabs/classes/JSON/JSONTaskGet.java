package com.sensolabs.tabs.classes.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.sensolabs.tabs.classes.HttpClient1;
import com.sensolabs.tabs.classes.Persistenses;
import com.sensolabs.tabs.classes.SensoStrings;

public class JSONTaskGet implements Runnable {

	private String p_id;
	private Activity act;

	public JSONTaskGet(String p_id, Activity act) {
		this.p_id = p_id;
		this.act = act;
	}

	@Override
	public void run() {

		GetJsonDataFromServer(SensoStrings.URLGet + p_id);

	}

	private void GetJsonDataFromServer(String URL) {

		Persistenses pref = new Persistenses(act);

		// Log.i("Received Response", jsonObjRecv.toString(2));
		try {
			JSONObject jsonObjRecv = HttpClient1.SendHttpGET(URL);
			pref.putPatientID(jsonObjRecv.getString("p_id"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
