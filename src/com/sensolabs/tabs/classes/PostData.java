//package com.sensolabs.tabs.classes;
//
//import java.util.LinkedList;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class PostData extends AsyncTask<Void, Integer, Void> {
//		public static LinkedList<Integer> dataList = new LinkedList<Integer>();
//		public static boolean postdata = false;
//		private Activity activity;
//		
//		
//		public PostData(Activity activity, boolean post) {
//			this.activity = activity;
//			postdata = post;
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			while (postdata) {
//
//				int timeBeforePlot = (int) System.currentTimeMillis();
//				if (dataList.size() > 0) {
//
//					try {
//
//						Integer currentValue = dataList.get(0);
//						publishProgress(timeBeforePlot, currentValue);
//						dataList.remove(0);
//
//						Thread.sleep(30);
//					} catch (Throwable e) {
//						Log.e("postValue", "sleep error");
//						e.printStackTrace();
//					}
//				} else {
//
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			// Log.e("Fragment_Bluetooth_Main.PostData", "onProgressUpdate");
//			if (values[1] != null) {
//				int[] extra = new int[2];
//				extra[0] = values[0];
//				extra[1] = values[1];
//				Intent intent = new Intent("BLUETOOTH_DATA");
//				intent.putExtra("BLUETOOTH_DATA_STREAM", extra);
//				// Log.e("wichtig", "wave intent sent");
//
//				activity.sendBroadcast(intent);
//
//				super.onProgressUpdate(values);
//			}
//		}
//
//		@Override
//		protected void onPreExecute() {
//			Log.e("Fragment_Bluetooth_Main.PostData", "onPreExecute");
//			super.onPreExecute();
//		}
//
//	}