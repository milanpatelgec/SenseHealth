
package com.sensolabs.tabs;


import static com.example.bleexample.classic.IntentIdentifier.BLUETOOTH_UPDATES;

import java.math.BigInteger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;





//
//import com.actionbarsherlock.app.SherlockFragment;
//import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.FillDirection;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.example.bleexample.R;
import com.sensolabs.tabs.classes.Persistenses;
import com.sensolabs.tabs.classes.SensoStrings;



public class FragmentBluetoothGraph extends Fragment {
	
	private static final String TAG = FragmentBluetoothGraph.class.getSimpleName();

	private static XYPlot plot1 = null;
	static private SimpleXYSeries series1 = null;
	private static Activity activity;
	
	private Persistenses persistenses;
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		activity.unregisterReceiver(messageReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		activity.registerReceiver(messageReceiver, new IntentFilter(BLUETOOTH_UPDATES));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bluetooth_graph, container, false);
        activity = getActivity();

		plot1 = (XYPlot) view.findViewById(R.id.dynamicPlotGraph);

		persistenses = new Persistenses(activity);
		
		invokeGraph();

		return view;
	}
	
	private void invokeGraph(){
		
		if(persistenses.getDeviceName().matches(SensoStrings.DEVICE_PULSE_OXIMETER)){
			graphPulseReq();
		}else if (persistenses.getDeviceName().matches(SensoStrings.DEVICE_EASY_ECG)){
			graphECGReq();
		}else{
			Log.d(TAG, "Device is not found");
		}
	}
	
	private void graphPulseReq(){
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
	
	private void graphECGReq(){
		series1 = new SimpleXYSeries("1");
		series1.useImplicitXVals();
		plot1.setRangeBoundaries(-90000, +90000, BoundaryMode.FIXED);
	//	plot1.setRangeBoundaries(-1.5, 1.5, BoundaryMode.FIXED);
	//	plot1.setDomainBoundaries(0, 100, BoundaryMode.AUTO); 
	//plot1.setDomainBoundaries(0, 100, BoundaryMode.AUTO);
	
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
		plot1.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 10);
		
	
	}

	private static BroadcastReceiver messageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			int spO2_val = 0;
			int heartrate_val = 0;
			int batLvl_val = 0;
			int perfusion_index = 0;
			String wave0, wave1, wave2, wave3, wave4, EcgWave;
			String[] dataBytes;

			Bundle extras = intent.getExtras();
			if (extras.containsKey(SensoStrings.DATA_RECEIVED_INTENT)
					&& extras.containsKey(SensoStrings.DATA_RECEIVED_INTENT)) {
				String data = (intent
						.getStringExtra(SensoStrings.DATA_RECEIVED_INTENT));
				int bytesAvaiable = intent.getIntExtra(
						SensoStrings.BYTE_RECEIVED_INTENT, 1);

				dataBytes = data.split("x");
			//	 Log.i(TAG, data);

				Persistenses persistenses = new Persistenses(context);

				if (persistenses.getDeviceName().matches(
						SensoStrings.DEVICE_PULSE_OXIMETER)) {
					

					if (bytesAvaiable == 12) {

						spO2_val = Integer.parseInt(dataBytes[5], 16);
						heartrate_val = Integer.parseInt(
								dataBytes[7].concat(dataBytes[6]), 16);
						perfusion_index = Integer.parseInt(dataBytes[8], 16);

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
						StringBuilder wave_string = new StringBuilder();

						if (beatCheck.contains("11111")) {

						//	int[] wave = new int[50];
							for (int i = 5; i < 10; i++) {
								int parseInt = Integer.parseInt(dataBytes[i],16);
							//	wave[i - 5] = (parseInt);
								UpdatePlotPulseOx(parseInt / 8);
								wave_string.append(parseInt + ",");

							}
						}

					}
				} else if (persistenses.getDeviceName().matches(
						SensoStrings.DEVICE_EASY_ECG)) {
				
					
					heartrate_val = Integer.parseInt(dataBytes[54], 16);
					int[] wave = new int[25];
					float parseInt;
					for (int i = 4; i < 54; i++) {
						if(i<53 && i%2==0){
								 UpdatePlotECG( get2sComplement(dataBytes[i].concat(dataBytes[i+1])));
						}
					  }
					}
			}

		}

	};
	
	public static int get2sComplement(String data){
		String temp = hexToBin(data);
		int getData =0, int16= -32768;
		String resultString = "";
		StringBuilder sb ;
		if(temp.length()!=16){
			getData = Integer.parseInt(temp,2);
			return getData;
		} else if(temp.length()==16){
			sb = new StringBuilder(temp);
			sb.deleteCharAt(0);
			resultString = sb.toString();
			getData = Integer.parseInt(resultString, 2);
			return (int16 + getData);
			
		}
		return 0;
	}
	
	public static void UpdatePlotPulseOx(float value) {

		if (series1.size() > 220) {
			series1.removeFirst();
		}
		
		if(value<=15){
		//if (value <= 0.5) {
			series1.addLast(null, value);
			plot1.redraw();
		}

	}
	
	public static void UpdatePlotECG(float value) {

		if (series1.size() > 120) {
			series1.removeLast();
		}
		
			series1.addFirst(null, value);
			plot1.redraw();
	}

	static String hexToBin(String s) {
		return new BigInteger(s, 16).toString(2);
	}

}