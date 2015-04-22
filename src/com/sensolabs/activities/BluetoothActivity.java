package com.sensolabs.activities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
//import com.example.cardioapp.database.SetValue.connectToServer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.example.bleexample.R;
import com.example.bleexample.classic.ClassicService;
import com.sensolabs.tabs.FragmentBluetoothGraph;
import com.sensolabs.tabs.GetSensorDataFragment;
import com.sensolabs.tabs.SetValue;
import com.sensolabs.tabs.classes.Persistenses;
import com.sensolabs.tabs.classes.SensoStrings;
import com.sensolabs.tabs.classes.TabsAdapter;
import com.sensolabs.tabs.classes.postToHttp;



public class BluetoothActivity extends FragmentActivity {

	private final static String TAG = BluetoothActivity.class.getSimpleName();
            Activity act;
			ViewPager mViewPager;
			TabsAdapter mTabsAdapter;
			TextView tabCenter;
			TextView tabText;
			private static ActionBar bar;
			private static Drawable d;
		//	private Intent intentDevice;
		//	ClassicService mService;
			
		//	private Persistenses persistenses;
			boolean mBound = false;
			public boolean mConnect = false;
			//private Intent intentDevice;
			private static postToHttp doPost;
			
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
			//	d = getResources().getDrawable(R.drawable.background);
				mViewPager = new ViewPager(this);
				mViewPager.setId(R.id.pager);

				setContentView(mViewPager);
				bar = getActionBar();
				bar.setTitle("Vital Data");
				bar.setDisplayHomeAsUpEnabled(true);
				bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

				bar.setDisplayShowTitleEnabled(true);
				bar.setDisplayShowHomeEnabled(true);
			//	bar.setBackgroundDrawable(d);
			//	persistenses = new Persistenses(getApplicationContext());
			//	intentDevice = getIntent();
			//	persistenses.putDeviceName(intentDevice.getStringExtra(SensoStrings.EXTRAS_DEVICE_NAME));
				//persistenses.putDeviceAddress(intentDevice.getStringExtra(SensoStrings.EXTRAS_DEVICE_ADDRESS));
				
			//	Intent intent = new Intent(this, ClassicService.class);
			//	bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

				try {
					Field actionBarField = bar.getClass().getDeclaredField("mActionBar");
					actionBarField.setAccessible(true);
					enableEmbeddedTabs(actionBarField.get(bar));
				} catch (Exception e) {
					Log.e("tag", "Error enabling embedded tabs", e);
				}

				mTabsAdapter = new TabsAdapter(this, mViewPager);

				mTabsAdapter.addTab(bar.newTab().setText("LIVE-DATA"), GetSensorDataFragment.class, null);

			    mTabsAdapter.addTab(bar.newTab().setText("GRAPH"), FragmentBluetoothGraph.class, null);
				
				mTabsAdapter.addTab(bar.newTab().setText("SET-VALUES"), SetValue.class, null);
				
				//mTabsAdapter.addTab(bar.newTab().setText("CHATTING"), chatWithServer.class, null);
//				connect = new connectToServer();
//				connect.execute("");
//				  getSupportFragmentManager().beginTransaction().addToBackStack(null)
//	                .add(0,FragmentBluetoothMain()).commit();
			}

			private void enableEmbeddedTabs(Object actionBar) {
				try {
					Method setHasEmbeddedTabsMethod = actionBar.getClass().getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
					setHasEmbeddedTabsMethod.setAccessible(true);
					setHasEmbeddedTabsMethod.invoke(actionBar, true);
				} catch (Exception e) {
					Log.e("Tag", "Error marking actionbar embedded", e);
				}
			}

	
//
//			@Override
//			public void onBackPressed() {
//				finish();
//				super.onBackPressed();
//			}
			
//			public boolean onCreateOptionsMenu(Menu menu) {
//				getMenuInflater().inflate(R.menu.menu_connect, menu);
//				if (!mConnect) {
//					menu.findItem(R.id.bl_disconnect).setVisible(false);
//					menu.findItem(R.id.bl_connect).setVisible(true);
//					// menu.findItem(R.id.bl_refresh).setActionView(null);
//				} else {
//					menu.findItem(R.id.bl_disconnect).setVisible(true);
//					menu.findItem(R.id.bl_connect).setVisible(false);
//					// menu.findItem(R.id.bl_refresh).setActionView(
//					// R.layout.actionbar_indeterminate_progress);
//				}
//				return true;
//			}
//
//			@Override
//			public boolean onOptionsItemSelected(MenuItem item) {
//				switch (item.getItemId()) {
//				case R.id.bl_connect:
//					connect();
//					break;
//				case R.id.bl_disconnect:
//					disconnect();
//					break;
//				}
//				return true;
//			}
//			

}
