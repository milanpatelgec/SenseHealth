package com.sensolabs.tabs.classes.JSON;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.sensolabs.tabs.classes.HttpClient1;
import com.sensolabs.tabs.classes.SensoStrings;
import com.sensolabs.tabs.classes.object.JPatient;
import com.sensolabs.tabs.databases.Patient;

public class JSONTaskPost implements Runnable {

	private final static String TAG = JSONTaskPost.class.getSimpleName();

	Patient patient;
	Activity act;

	public JSONTaskPost(Patient patient, Activity act) {
		this.patient = patient;
		this.act = act;
	}

	@Override
	public void run() {
		sendJsonDataToServer(patient, SensoStrings.URLPost);
	}

	private void sendJsonDataToServer(Patient p, String URL) {

		JPatient jPatient = new JPatient();

		JSONObject jsonObjSend = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Long arrivedTime = (Long) (new Date().getTime());

		try {
			// Add key/value pairs
			jsonObjSend.put("p_id", p.getId());
			jsonObjSend.put("fname", p.getFirstName());
			jsonObjSend.put("lname", p.getName());
			jsonObjSend.put("arrived", arrivedTime);
			jsonArray.put("ECG");

			jsonObjSend.put("categories", jsonArray);

			// Output the JSON object we're sending to Logcat:
			Log.i(TAG + ":JSON Data", jsonObjSend.toString(2));

			// Send the HttpPostRequest and receive a JSONObject in return
			JSONObject jsonObjRecv = HttpClient1.SendHttpPost(URL, jsonObjSend);
			Log.i("Received Response", jsonObjRecv.toString(2));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}