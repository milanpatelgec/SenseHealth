package com.sensolabs.tabs.classes;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Post data to http-Server
 * 
 * 
 */
public class postToHttp extends AsyncTask<Void, String, Void> {
	private static final String TAG = postToHttp.class.getSimpleName();
	private List<NameValuePair> nameValuePairs;
	private HttpClient httpclient;
	private HttpPost httppost;
	private StringBuilder sb;
	public static boolean post = true;
	private static boolean pWave = false, pHeart = false;

	public postToHttp(Activity activity) {
		nameValuePairs = new ArrayList<NameValuePair>();
		httpclient = new DefaultHttpClient();

		// httppost = new
		// HttpPost("http://"+BluetoothActivity.serverName+"/kardioAppServer/remotestation");
		post = true;
		if (isNetworkAvailable(activity)) {
			Log.d(TAG, "Network is available");
		//	httppost = new HttpPost("http://192.168.185.1:8080/health/remotestation");//
			httppost = new HttpPost("http://192.168.6.23:8181/si-server/remotestation");
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				execute();
			}
		} else
			Log.e("Http", "Network is Not Avaiable");
	}

	public void setNewPostOxGraph(String stringWave, boolean graph) {
		BasicNameValuePair bWave = new BasicNameValuePair("pulseox_datastream",stringWave);
		BasicNameValuePair bGraph = new BasicNameValuePair("true_graph",String.valueOf(graph));
		if (nameValuePairs.size() == 0 && graph ==true) {
			nameValuePairs.add(bWave);
			nameValuePairs.add(bGraph);
		}
	}
	
	public void setNewPost(int bytesAvaiable,String data_stream,  String min, String max, 
			String patientID, String setMessage, String serverMessage,
			String minYellow, String maxYellow, boolean pulse, boolean ecg) {
		BasicNameValuePair bBytes= new BasicNameValuePair("avaiable_bytes", String.valueOf(bytesAvaiable));
		BasicNameValuePair bStream = new BasicNameValuePair("data_stream", data_stream);
		BasicNameValuePair bMin = new BasicNameValuePair("min_heart", min);
		BasicNameValuePair bMax = new BasicNameValuePair("max_heart", max);
		BasicNameValuePair bID = new BasicNameValuePair("patient_id", patientID);
		BasicNameValuePair bMSG = new BasicNameValuePair("set_message",	setMessage);
		BasicNameValuePair bMessage = new BasicNameValuePair("type_message",serverMessage);
		BasicNameValuePair bMinYellow = new BasicNameValuePair("min_yellow",minYellow);
		BasicNameValuePair bMaxYellow = new BasicNameValuePair("max_yellow",maxYellow);
		BasicNameValuePair btPulse = new BasicNameValuePair("true_pulse",String.valueOf(pulse));
		BasicNameValuePair btEcg = new BasicNameValuePair("true_ecg",String.valueOf(ecg));
		if (nameValuePairs.size() == 0 ) {
			nameValuePairs.add(bBytes);
			 nameValuePairs.add(bStream);
			 nameValuePairs.add(bMin);
			nameValuePairs.add(bMax);
			 nameValuePairs.add(bID);
			 nameValuePairs.add(bMSG);
			 nameValuePairs.add(bMessage);
			 nameValuePairs.add(bMinYellow);
			 nameValuePairs.add(bMaxYellow);
			 nameValuePairs.add(btPulse);
			 nameValuePairs.add(btEcg);
		}
	}


	public boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.i("isNetworkAvailable", "ConnectivityManager");
		if (!connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnected()) {// by doing this app won't crash if there is
									// Wifi or Network not avaible..
			Toast.makeText(ctx, "Network is not avaiable", Toast.LENGTH_SHORT).show();
			return false;
		} else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnected()
				|| connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
						.isConnected()) {
			// Log.e("isNetworkAvailable", "ConnectivityManager");
			return true;
		} else
			return false;

		// return false;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		//Log.e("Fragment_Bluetooth_Main.SimulationTask", "doInBackground");
		while (true) {

			if (nameValuePairs.size() == 11) {

				try {
					// Add your data
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					// Execute HTTP Post Request
					httpclient.execute(httppost);
					nameValuePairs.removeAll(nameValuePairs);

				} catch (Exception e) {
					// Log.e("DoPost", "exeption!!!");
					// e.printStackTrace();
					// cancel(true);
			}
			}
		}
	}

}
