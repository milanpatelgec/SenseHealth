package com.sensolabs.tabs;

import static com.example.bleexample.classic.IntentIdentifier.BLUETOOTH_UPDATES;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.FillDirection;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.example.bleexample.R;
import com.example.bleexample.classic.ClassicService;
import com.example.bleexample.classic.ClassicService.LocalBinder;
import com.sensolabs.tabs.classes.HttpClient1;
import com.sensolabs.tabs.classes.Persistenses;
import com.sensolabs.tabs.classes.SensoStrings;

public class GetSensorDataFragment extends Fragment {

	private final static String TAG = GetSensorDataFragment.class
			.getSimpleName();

	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	private TextView tvSpo2, tvHeartrate, tv_spo2_unit, tv_heartrate_unit,
			tv_perfusion_unit, tv_perfusion;
	public static String patientID = "", minThre = "", maxThre = "",
			sendMSG = "", ServerMessage = "", minYellow = "", maxYellow = "";
	private static ImageView heart;// battery;
	public static TextView tvSpo2Value;
	public static TextView tvHeartrateValue;
	public static TextView tvPerfusionValue;
	private static ProgressBar spo2IndicatorBar, perfusionIndicatior;
	private static XYPlot plot1 = null;
	private static SimpleXYSeries series1 = null;
	// private static Activity activity;
	private static TextSwitcher textSwitcher;
	private static ImageView iconState;
	private float[] roundedCorners;
	private static ShapeDrawable pgDrawable;
	protected static Animation heartAnimUp, heartAnimDown,
			heartAnimPause = null;
	protected static AnimationSet set = null;
	static Activity activity;
	ClassicService mService;
	boolean mBound = false;
	public boolean mConnect = false;
	private Intent intentDevice;
//	private static postToHttp doPost;
	// private static PostHeartOxi doHeartOx;
	private static Button bt_connect;

	private Persistenses persistenses;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bluetooth_main,
				container, false);
		activity = getActivity();

		iconState = (ImageView) view.findViewById(R.id.iv_state);

		bt_connect = (Button) view.findViewById(R.id.bt_connet);

		// Typeface tfNormal = Typeface.createFromAsset(getAssets(),
		// "fonts/DIGITALDREAMFAT.ttf");

		tvHeartrate = (TextView) view.findViewById(R.id.tv_heartrate);
		tvHeartrate.setText(getString(R.string.heartrate_title));
		tvHeartrate.setTypeface(tvHeartrate.getTypeface(), Typeface.ITALIC);
		tvHeartrate
				.setTextSize(10 * getResources().getDisplayMetrics().density);
		tvHeartrateValue = (TextView) view
				.findViewById(R.id.tv_heartrate_value);
		tvHeartrateValue.setText("0");
		// tvHeartrateValue.setTypeface(tfNormal);
		tvHeartrateValue
				.setTextSize(16 * getResources().getDisplayMetrics().density);
		tvHeartrateValue.setTextColor(Color.BLUE);
		tv_heartrate_unit = (TextView) view
				.findViewById(R.id.tv_heartrate_unit);
		tv_heartrate_unit.setTextColor(Color.GRAY);
		tv_heartrate_unit
				.setTextSize(5 * getResources().getDisplayMetrics().density);

		tvSpo2 = (TextView) view.findViewById(R.id.tv_spO2);
		tvSpo2.setText(getString(R.string.spo2_title));
		tvSpo2.setTextSize(10 * getResources().getDisplayMetrics().density);
		tvSpo2.setTypeface(tvSpo2.getTypeface(), Typeface.ITALIC);
		tvSpo2Value = (TextView) view.findViewById(R.id.tv_spO2_value);
		tvSpo2Value.setText("0");
		tvSpo2Value
				.setTextSize(16 * getResources().getDisplayMetrics().density);
		tvSpo2Value.setTextColor(Color.BLUE);
		// tvSpo2Value.setTypeface(tfNormal);
		tv_spo2_unit = (TextView) view.findViewById(R.id.tv_spo2_unit);
		tv_spo2_unit.setTextColor(Color.GRAY);
		tv_spo2_unit
				.setTextSize(5 * getResources().getDisplayMetrics().density);

		heart = (ImageView) view.findViewById(R.id.iv_heart);
		textSwitcher = (TextSwitcher) view.findViewById(R.id.textSwitcher1);
		textSwitcher.clearAnimation();

		spo2IndicatorBar = (ProgressBar) view.findViewById(R.id.pb_spo2);
		// battery = (ImageView)view. findViewById(R.id.iv_battery);

		roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
		pgDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null,
				null));

		ClipDrawable progress = new ClipDrawable(pgDrawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);
		spo2IndicatorBar.setProgressDrawable(progress);
		spo2IndicatorBar.setBackgroundDrawable(this.getResources().getDrawable(
				android.R.drawable.progress_horizontal));

		// specify the in/out animations you wish to use
		textSwitcher.setInAnimation(activity, android.R.anim.slide_out_right);
		// textSwitcher.setOutAnimation(activity, android.R.anim.slide_in_left);

		// provide two TextViews for the TextSwitcher to use
		// you can apply styles to these Views before adding
		textSwitcher.addView(new TextView(activity));
		textSwitcher.addView(new TextView(activity));
		setAnimation(1);
		heart.startAnimation(heartAnimUp);

		// you are now ready to use the TextSwitcher
		// it will animate between calls to setText

		plot1 = (XYPlot) view.findViewById(R.id.dynamicPlot);

		// intentDevice = activity.getIntent();

		persistenses = new Persistenses(activity);

		// doHeartOx = new PostHeartOxi(activity);

		invokeGraph();

		Intent intent = new Intent(getActivity(), ClassicService.class);
		getActivity()
				.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		bt_connect.setOnClickListener(onclickConnectButton(view));

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		minYellow = "5";
		maxYellow = "5";
		Intent intent = new Intent(getActivity(), ClassicService.class);
		getActivity()
				.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	private OnClickListener onclickConnectButton(View view) {
		// TODO Auto-generated method stub
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				connect();
			}
		};
	}

	private void invokeGraph() {
		// persistenses.putDeviceName(intentDevice.getStringExtra(EXTRAS_DEVICE_NAME));
		// persistenses.putDeviceAddress(intentDevice.getStringExtra(EXTRAS_DEVICE_ADDRESS));
		Log.d(TAG, "Name:- " + persistenses.getDeviceName() + " "
				+ "Address:- " + persistenses.getDeviceAddress());

		if (persistenses.getDeviceName().matches(
				SensoStrings.DEVICE_PULSE_OXIMETER)) {
			graphPulseReq();
		} else if (persistenses.getDeviceName().matches(
				SensoStrings.DEVICE_EASY_ECG)) {
			graphECGReq();
		} else {
			Log.d(TAG, "Device is not found");
		}
	}

	private void graphPulseReq() {
		series1 = new SimpleXYSeries("1");
		series1.useImplicitXVals();
		plot1.setRangeBoundaries(0, 25, BoundaryMode.FIXED);
		plot1.setDomainBoundaries(-100, 100, BoundaryMode.AUTO);
		plot1.addSeries(series1, new LineAndPointFormatter(Color.GRAY, null,
				Color.CYAN, null, FillDirection.BOTTOM));
		plot1.setDomainStepValue(5);
		plot1.setTicksPerRangeLabel(3);
		plot1.setDomainLabel("Sec");
		plot1.getDomainLabelWidget().pack();
		plot1.setRangeLabel("Puls");
		plot1.getRangeLabelWidget().pack();
		plot1.setTitle("PULSEBAR");

	}

	private void graphECGReq() {
		series1 = new SimpleXYSeries("1");
		series1.useImplicitXVals();
		 plot1.setRangeBoundaries(-50000, +50000, BoundaryMode.FIXED);
		 plot1.setDomainBoundaries(-100, 100, BoundaryMode.AUTO);
		// plot1.setRangeBoundaries(-1.5, 1.5, BoundaryMode.FIXED);
		// plot1.setDomainBoundaries(0, 100, BoundaryMode.AUTO);
		// plot1.setDomainBoundaries(0, 100, BoundaryMode.AUTO);

		/**
		 * LineAndPointFormatter(Integer lineColor, Integer vertexColor, Integer
		 * fillColor, PointLabelFormatter plf, FillDirection fillDir)
		 * 
		 */

		plot1.addSeries(series1, new LineAndPointFormatter(Color.GREEN, null,
				Color.TRANSPARENT, null, FillDirection.BOTTOM));

		plot1.setDomainStepValue(15);
		plot1.setTicksPerRangeLabel(3);
		plot1.setDomainLabel("Time/s");
		plot1.getDomainLabelWidget().pack();
		plot1.setRangeLabel("Surface potential/mV");
		plot1.getRangeLabelWidget().pack();
		plot1.setTitle("ECG graph");
		plot1.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
	}

	@Override
	public void onResume() {
		super.onResume();
		// connect();
		getActivity().registerReceiver(messageReceiver,
				new IntentFilter(BLUETOOTH_UPDATES));

	}

	@Override
	public void onStop() {
		super.onStop();
		// Unbind from the service

	}

	private void disconnect() {
		if (mService.checkConnection()) {
			mConnect = false;
			Log.i("onDisconnect", String.valueOf(mService.closeBT()));
		} else {
			Toast.makeText(getActivity(), "Device is already disconnected",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void connect() {

		if (mBound) {
			mService.putDeviceName(persistenses.getDeviceName());

		//	doPost = new postToHttp(activity);

			Log.d(TAG,
					"Connecting to sensor..." + persistenses.getDeviceAddress());
			ConnectTask connectTask = new ConnectTask(
					persistenses.getDeviceAddress());// ;44:47");//"F4:B7:E2:37:E3:5A");//
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				connectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				connectTask.execute();
			}
			iconState.setImageResource(R.drawable.signal);
			mConnect = true;
		} else {
			Log.e(TAG, "Bluetooth is not connected");
			mConnect = false;
		}
	}

	private class ConnectTask extends AsyncTask<Void, Void, Boolean> {
		boolean connected = false;

		// ProgressDialog dialog;

		public ConnectTask(String adress) {
			mService.setBtDeviceByAdress(adress);
			// dialog = new ProgressDialog(getApplicationContext());
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// dialog.dismiss();
			// bluetoothConnectionState(result);

			if (result = true) {
				Log.i("connect Result", String.valueOf(result));
				proceedOnDevice(result);
				iconState.setColorFilter(Color.BLUE);
			} else {
				Log.i("ConnectTask", String.valueOf(result));
			}

			// invalidateOptionsMenu();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				connected = mService.connect();
				// invalidateOptionsMenu();
			} catch (Throwable e) {
				connected = false;
				Log.i("onPreEx", String.valueOf(connected));
				Log.i("Throwable", e.getMessage());
			}
			return connected;
		}
	}

	public void getEasyECG() {
		mService.setECG(SensoStrings.ECG_PARAMETER);
		// mService.sendCommandVersionInfo("A5", "55", "01", "00", "00");
		textSwitcher.setText("getting ECG waveform! \n");
	}

	public void getParameter() {
		mService.setData(SensoStrings.PULSE_PARAMETER);
		mService.sendCommandVersionInfo("aa" + "55", "0F", "03", "84" + "01",
				"00");
		textSwitcher.setText("command get parameter data sent! \n");
	}

	public void getWaveform() {
		mService.setWave(SensoStrings.PULSE_WAVEFORM);
		mService.sendCommandVersionInfo("aa" + "55", "0F", "03", "85" + "01",
				"00");
		textSwitcher.setText("Getting Data from Sensor \n");
	}

	public void proceedOnDevice(Boolean result) {
		if (result) {
			if (mService.beginListenForData()) {
				// mService.sendInquire();
				// getEasyECG();
				if (persistenses.getDeviceName().matches(
						SensoStrings.DEVICE_PULSE_OXIMETER)) {
					getParameter();
					getWaveform();
				} else if (persistenses.getDeviceName().matches(
						SensoStrings.DEVICE_EASY_ECG)) {
					getEasyECG();
				} else {
					Log.e(TAG, "Nothing Found");
				}
			} else {
				Log.d("ProceedOnDevice", "Not listening for data");
			}
		} else {
			Log.d("ProceedOnDevice", "connecting result if false");
		}

	}

	private void getWave() {
		mService.sendCommandVersionInfo("a5", "55", "01", "00", "00");
		textSwitcher.setText("Getting Data from ECG \n");

	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;

			Log.e("mConnection", String.valueOf(binder.isBinderAlive()));
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
			Log.i("onServiceDisconnected", String.valueOf(mService.closeBT()));
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBound) {
			// mService.closeBT();
			getActivity().unbindService(mConnection);
			mBound = false;
		}
		getActivity().unregisterReceiver(messageReceiver);
		persistenses.putDeviceName("");
		persistenses.putDeviceAddress("");
		persistenses.putPatientID("");

		Log.i("onDestroy", String.valueOf(mService.closeBT()));

	}

	public static void setAnimation(double bps) {
		if (bps > 0) {

			int duration = ((int) (1000 / bps) / 3);

			int diff = (duration / 4);
			heartAnimUp = new ScaleAnimation(1, 1.3f, 1, 1.3f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			heartAnimUp.setDuration(duration - diff);
			heartAnimDown = new ScaleAnimation(1.3f, 1, 1.3f, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			heartAnimDown.setDuration(duration - diff);
			heartAnimPause = new ScaleAnimation(1, 1, 1, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			heartAnimPause.setDuration(duration + (2 * diff));
			heartAnimUp.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					heart.startAnimation(heartAnimDown);
				}
			});
			heartAnimDown.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					heart.startAnimation(heartAnimPause);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationStart(Animation animation) {
				}
			});
			heartAnimPause.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					heart.startAnimation(heartAnimUp);
				}
			});
		}
	}

	public static void setSpO2Indicator(int spo2) {
		if (spo2 >= 96) {
			pgDrawable.getPaint().setColor(Color.GREEN);
			tvSpo2Value.setTextColor(Color.BLUE);
		} else if (spo2 >= 92) {
			// pgDrawable.getPaint().setColor(getResources().getColor(R.color.orange));
			tvSpo2Value.setTextColor(Color.YELLOW);
		} else {
			pgDrawable.getPaint().setColor(Color.RED);
			tvSpo2Value.setTextColor(Color.RED);
		}

		spo2IndicatorBar.setProgress(spo2);
	}

	// public static void setBatteryLevel(int batLvl) {
	// if (batLvl == 0) {
	// battery.setImageResource(R.drawable.battery_low);
	// } else if (batLvl == 2) {
	// battery.setImageResource(R.drawable.battery_half);
	// } else if (batLvl == 3) {
	// battery.setImageResource(R.drawable.battery_full);
	// }
	// }

	private static BroadcastReceiver messageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			int spO2_val = 0;
			int heartrate_val = 0;
			int batLvl_val = 0;
			int perfusion_index = 0;
			String wave0, wave1, wave2, wave3, wave4, EcgWave;
			String[] dataBytes;
			StringBuilder wave_string = null;
			boolean pulse = false, ecg = false;

			Bundle extras = intent.getExtras();
			if (extras.containsKey(SensoStrings.DATA_RECEIVED_INTENT)
					&& extras.containsKey(SensoStrings.DATA_RECEIVED_INTENT)) {
				String data = (intent
						.getStringExtra(SensoStrings.DATA_RECEIVED_INTENT));
				int bytesAvaiable = intent.getIntExtra(
						SensoStrings.BYTE_RECEIVED_INTENT, 1);

				dataBytes = data.split("x");
				// Log.i(TAG, data);

				Persistenses persistenses = new Persistenses(context);

				if (persistenses.getDeviceName().matches(
						SensoStrings.DEVICE_PULSE_OXIMETER)) {

					ecg = false;
					pulse = true;
					// doPost.setNewPost(bytesAvaiable, data, minThre, maxThre,
					// patientID,sendMSG, ServerMessage, minYellow, maxYellow);
				//	doPost.setNewPost(bytesAvaiable, data, minThre, maxThre,
				//			patientID, sendMSG, ServerMessage, minYellow,
					//		maxYellow, pulse, ecg);
					
					if(persistenses.getPatientID().matches(patientID) && patientID!=null && !persistenses.getPatientID().matches("")){
			//			new Thread(new TaskGet(persistenses.getPatientID(), data)).start();
			//			Log.d("Json Data Check" , patientID +" !!"+ persistenses.getPatientID());
						new Thread(new TaskGet(persistenses.getPatientID(), data, String.valueOf(pulse),String.valueOf(ecg),
								String.valueOf(bytesAvaiable))).start();
					}

					if (bytesAvaiable == 12) {


						spO2_val = Integer.parseInt(dataBytes[5], 16);
						heartrate_val = Integer.parseInt(
								dataBytes[7].concat(dataBytes[6]), 16);
						perfusion_index = Integer.parseInt(dataBytes[8], 16);

						Time timeOfSys = new Time();
						timeOfSys.setToNow();
						int second = timeOfSys.second;

						tvSpo2Value.setText("" + spO2_val);
						tvHeartrateValue.setText("" + heartrate_val);

						setAnimation(heartrate_val / 60);
						setSpO2Indicator(spO2_val);
						// setBatteryLevel(batLvl_val);
						textSwitcher("Connected to Pulse-Oximeter!!");
						// doHeartOx.setNewPost(heartrate_val, minThre, maxThre,
						// patientID, sendMSG, ServerMessage, minYellow,
						// maxYellow, heart);
					}

					if (bytesAvaiable == 11) {
						wave0 = hexToBin(dataBytes[5]);
						wave1 = hexToBin(dataBytes[6]);
						wave2 = hexToBin(dataBytes[7]);
						wave3 = hexToBin(dataBytes[8]);
						wave4 = hexToBin(dataBytes[9]);

						char w0 = wave0.charAt(0);
						char w1 = wave1.charAt(0);
						char w2 = wave2.charAt(0);
						char w3 = wave3.charAt(0);
						char w4 = wave4.charAt(0);

						String beatCheck = String.valueOf(w0)
								+ String.valueOf(w1) + String.valueOf(w2)
								+ String.valueOf(w3) + String.valueOf(w4);
						wave_string = new StringBuilder();

						if (beatCheck.contains("11111")) {

							// int[] wave = new int[50];
							for (int i = 5; i < 10; i++) {
								int parseInt = Integer.parseInt(dataBytes[i],
										16);
								// wave[i - 5] = (parseInt);
								UpdatePlotPulseOx(parseInt / 8);
								wave_string.append(parseInt / 8 + ",");

							}
							
							wave_string.deleteCharAt(wave_string.length() - 1);

							// doPost.setNewPostOxGraph(wave_string.toString(),
							// graph);
						}

					}

				

				} else if (persistenses.getDeviceName().matches(
						SensoStrings.DEVICE_EASY_ECG)) {
					ecg = true;
					pulse = false;
					wave_string = new StringBuilder();
					heartrate_val = Integer.parseInt(dataBytes[54], 16);
					// Log.i(TAG, hexToBin(dataBytes[55]));
					// String leadStatus= hexToBinary(dataBytes[55]);

					// if(leadStatus.startsWith("0", 0)){
					tvHeartrateValue.setText("" + heartrate_val);
					setAnimation(heartrate_val / 60);
					int[] wave = new int[25];
					float parseInt;
					for (int i = 4; i < 53; i++) {
						if (i % 2 == 0) {
							int ecgInt = get2sComplement(dataBytes[i].concat(dataBytes[i+1]));
							UpdatePlotECG(ecgInt);
							wave_string.append(ecgInt + ",");
						}
					}
					
				String dataECG = wave_string.deleteCharAt(wave_string.length()-1).toString();
				
				Log.i("ECG Data", dataECG);
					
			//	doPost.setNewPost(heartrate_val, dataECG, minThre, maxThre,
			//			patientID, sendMSG, ServerMessage, minYellow,
			//			maxYellow, pulse, ecg);

				}
			}

		}
		
		class TaskGet implements Runnable {
			 
			  String data, p_id, boolPulse, availableBytes,boolECG;
				 
				 public TaskGet(String p_id, String data, String boolPulse,String boolECG, String availableBytes ) {
					this.data = data;this.p_id = p_id;this.boolPulse = boolPulse; this.availableBytes = availableBytes;
					this.boolECG = boolECG;
				}
			     @Override
			     public void run() {
			    	 
			      sendJsonDataToServer(p_id, data, boolPulse,boolECG, availableBytes);
			      
			      }
			
			    }

	};
	

	
	private static void sendJsonDataToServer(String p_id, String data, String boolPulse,String boolECG, 
			String availableBytes) {
		
		
		JSONObject jsonObjSend = new JSONObject();

		try {
			// Add key/value pairs
			jsonObjSend.put("sensordata", data);
			jsonObjSend.put("p_id", p_id);
			jsonObjSend.put("boolPulse", boolPulse);
			jsonObjSend.put("boolECG", boolECG);
			jsonObjSend.put("availableBytes", availableBytes);
		
			
			// Output the JSON object we're sending to Logcat:
			//Log.i( TAG + ":JSON Data", jsonObjSend.toString(2));
			
			// Send the HttpPostRequest and receive a JSONObject in return
			JSONObject jsonObjRecv = HttpClient1.SendHttpPost(SensoStrings.URLSensor, jsonObjSend);
		//	Log.i("Received Response", jsonObjRecv.toString(2));
			

		} catch (JSONException e) {
			e.printStackTrace();
		}


	}

	public static void textSwitcher(String text) {
		textSwitcher.setText(text);
		iconState.setImageResource(R.drawable.signal);
		iconState.setColorFilter(Color.BLUE);

	}

	public static int get2sComplement(String data) {
		String temp = hexToBin(data);
		int getData = 0, int16 = -32768;
		String resultString = "";
		StringBuilder sb;
		if (temp.length() != 16) {
			getData = Integer.parseInt(temp, 2);
			return getData;
		} else if (temp.length() == 16) {
			sb = new StringBuilder(temp);
			sb.deleteCharAt(0);
			resultString = sb.toString();
			getData = Integer.parseInt(resultString, 2);
			return (int16 + getData);

		}
		return 0;
	}

	static String hexToBin(String s) {
		return new BigInteger(s, 16).toString(2);
	}

	public static void UpdatePlotPulseOx(float value) {

		if (series1.size() > 120) {
			series1.removeFirst();
		}

		if (value <= 15) {
			// if (value <= 0.5) {
			series1.addLast(null, value);
			plot1.redraw();
		}

	}

	public static void UpdatePlotECG(float value) {

		if (series1.size() > 120) {
			series1.removeLast();
		}
		
		int i = 0;
	//	if(value<20000 && value > -20000){
		series1.addFirst(i, value);
		plot1.redraw();
		i=i+1;
	//	}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.menu_connect, menu);
	// if (!mConnect) {
	// menu.findItem(R.id.bl_disconnect).setVisible(false);
	// menu.findItem(R.id.bl_connect).setVisible(true);
	// // menu.findItem(R.id.bl_refresh).setActionView(null);
	// } else {
	// menu.findItem(R.id.bl_disconnect).setVisible(true);
	// menu.findItem(R.id.bl_connect).setVisible(false);
	// // menu.findItem(R.id.bl_refresh).setActionView(
	// // R.layout.actionbar_indeterminate_progress);
	// }
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.bl_connect:
	// connect();
	// break;
	// case R.id.bl_disconnect:
	// disconnect();
	// break;
	// }
	// return true;
	// }

}
