package com.sensolabs.tabs;

import static com.example.bleexample.classic.IntentIdentifier.BLUETOOTH_UPDATES;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bleexample.R;
import com.sensolabs.tabs.classes.Persistenses;
import com.sensolabs.tabs.classes.SensoStrings;
import com.sensolabs.tabs.databases.DatabaseHandler;
import com.sensolabs.tabs.databases.Patient;
import com.sensolabs.tabs.databases.SettingUpDialogue;

public class SetValue extends Fragment {
    
	Patient currentPatient= new Patient();
	Button Save, Load,hButton,iButton,bButton,Update;
	EditText firstName, name, patientID, minH, maxH, minI, maxI, minB, maxB, getMSG;
boolean alarm,message,autoSendValidation=true;
	private String nameET,firstNameET,pIDET;
	private String minHET,maxHET,minIET,maxIET,minBET,maxBET;
	Patient patient=new Patient();
	private static MediaPlayer ourAlarm;
	private  static Timer timer1,timer2;
	private Activity activity;
	private static int val_heart = 0, val_SpO2 = 0;

	
	
	 ///Method of inserting data in Database///
	/**
	 * This method is used for inserting data into the database. So this method take Patient's class' object as parameter
	 * and through getter it's taking values and through contentvalue's class' object it is putting all values in SQL table.
	 */
	public void insertData(Patient patient) {
		DatabaseHandler androidDbHelper = new DatabaseHandler(getActivity());
		SQLiteDatabase sql=androidDbHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(androidDbHelper.KEY_NAME, patient.getName());
		contentValues.put(androidDbHelper.KEY_HR, patient.getFirstName());
		contentValues.put(androidDbHelper.KEY_IBI, patient.getId());
		contentValues.put(androidDbHelper.KEY_HMIN, patient.getNumHeartRateMin());
		contentValues.put(androidDbHelper.KEY_HMAX, patient.getNumHeartRateMax());
		contentValues.put(androidDbHelper.KEY_IMIN, patient.getNumInterBeatIntervalMin());
		contentValues.put(androidDbHelper.KEY_IMAX, patient.getNumInterBeatIntervalMax());
		contentValues.put(androidDbHelper.KEY_BMIN, patient.getNumBreathingrateMin());
		contentValues.put(androidDbHelper.KEY_BMAX, patient.getNumBreathingrateMax());
		contentValues.put(androidDbHelper.KEY_ALARM, (patient.getAlarm())?1:0);//if true then 1 else 0
		contentValues.put(androidDbHelper.KEY_MESSAGE, (patient.getMessage())?1:0);
		sql.insert(androidDbHelper.NAME_TABLE, null, contentValues);
		//nullcolumnHack is for the Empty row.if we pass an empty contentValues then if we want to save then then nullColumnHack shouldn't be null
		sql.close();
			Toast.makeText(getActivity().getBaseContext(), "The Data has been stored" , Toast.LENGTH_SHORT).show();
		
	}
	/**
	 * This method is used for update the database if a user wants to change some fields. Here notice that we are ot updating 
	 * PatientID because it is a unique field.
	 * @return updated data
	 */
	private int updateData() {
		DatabaseHandler androidDbHelper = new DatabaseHandler(getActivity());
		SQLiteDatabase sql=androidDbHelper.getWritableDatabase();
		ContentValues contentValues= new  ContentValues();
		contentValues.put(androidDbHelper.KEY_NAME, firstName.getText().toString());
		contentValues.put(androidDbHelper.KEY_HR, name.getText().toString());
//		contentValues.put(androidDbHelper.KEY_IBI, patient.getId());
		contentValues.put(androidDbHelper.KEY_HMIN,minH.getText().toString());
		contentValues.put(androidDbHelper.KEY_HMAX, maxH.getText().toString());
		contentValues.put(androidDbHelper.KEY_IMIN, minI.getText().toString());
		contentValues.put(androidDbHelper.KEY_IMAX, maxI.getText().toString());
		contentValues.put(androidDbHelper.KEY_BMIN, minB.getText().toString());
		contentValues.put(androidDbHelper.KEY_BMAX, getMSG.getText().toString());
		contentValues.put(androidDbHelper.KEY_ALARM, SettingUpDialogue.indexAlarm);
		contentValues.put(androidDbHelper.KEY_MESSAGE, SettingUpDialogue.indexMessage);
		return sql.update(androidDbHelper.NAME_TABLE, contentValues,
				androidDbHelper.KEY_IBI+"='" + patientID.getText().toString()+"'"
				,null);
	}
	
	/**
	 * This method caleed when user want to retrive/load the old data or updated data from database where PatientID is unique.
	 * If PatientID has enter false or not valid then cursor.getCount() is less then zero and will make a toast
	 * @return
	 */
	DatabaseHandler populateData() {
		DatabaseHandler mDatabase= new DatabaseHandler(getActivity());
		SQLiteDatabase sql=mDatabase.getReadableDatabase();
		
		Cursor cursor = sql.query(mDatabase.NAME_TABLE, new String[]{mDatabase.KEY_NAME
			   ,mDatabase.KEY_HR,mDatabase.KEY_IBI,mDatabase.KEY_HMIN,mDatabase.KEY_HMAX
			   ,mDatabase.KEY_IMIN,mDatabase.KEY_IMAX,mDatabase.KEY_BMIN,mDatabase.KEY_BMAX,mDatabase.KEY_ALARM,mDatabase.KEY_MESSAGE}
		       ,mDatabase.KEY_IBI+"=?", new String[] {patientID.getText().toString()},null,null,null,null);
		//selectionArgs ::if we don't specify then we won't get primary where we are selectiong a table from..
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			currentPatient.setName(cursor.getString(0));
			currentPatient.setFirstName(cursor.getString(1));
			currentPatient.setId(cursor.getString(2));
			currentPatient.setNumHeartRateMin(cursor.getString(3));
			currentPatient.setNumHeartRateMax(cursor.getString(4));
			currentPatient.setNumInterBeatIntervalMin(cursor.getString(5));
			currentPatient.setNumInterBeatIntervalMax(cursor.getString(6));
			currentPatient.setNumBreathingrateMin(cursor.getString(7));
			currentPatient.setNumBreathingrateMax(cursor.getString(8));
			currentPatient.setAlarm((cursor.getInt(9)==1)?true:false);
			currentPatient.setMessage((cursor.getInt(10)==1)?true:false);

			GetSensorDataFragment.patientID=  SendPatientIDToServer(cursor.getString(2));
			String minString=cursor.getString(3);
			String maxString=cursor.getString(4);
			String msgForServer= cursor.getString(8);
			String minForYellow= cursor.getString(5);
			String maxForYellow= cursor.getString(6);
			if(!minString.isEmpty())
				GetSensorDataFragment.minThre=SendPatientIDToServer(minString);
			else
				GetSensorDataFragment.minThre="0";
			if(!maxString.isEmpty())
				GetSensorDataFragment.maxThre=SendPatientIDToServer(maxString);
			else
				GetSensorDataFragment.maxThre="0";
			if(!msgForServer.isEmpty())
				GetSensorDataFragment.ServerMessage=SendPatientIDToServer(msgForServer);
			else
				GetSensorDataFragment.ServerMessage=" ";
			if(!minForYellow.isEmpty())
				GetSensorDataFragment.minYellow=SendPatientIDToServer(minForYellow);
				else
					GetSensorDataFragment.minYellow="5";
			if(!maxForYellow.isEmpty())
				GetSensorDataFragment.maxYellow=SendPatientIDToServer(maxForYellow);
				else
					GetSensorDataFragment.maxYellow="5";
			
		}
		else
		{
			pIDET=patientID.getText().toString();
			currentPatient.setId(pIDET);
			patientID.setText(currentPatient.getId());
			Toast.makeText(getActivity(), "Record Not Found!", Toast.LENGTH_SHORT).show();

		}	
		cursor.close();
		sql.close();
		return mDatabase;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		final View myFragmentView = inflater.inflate(
		R.layout.set_value_fragment, container, false);
		
		activity = getActivity();
	

		hButton = (Button) myFragmentView.findViewById(R.id.btn1);
		hButton.setOnClickListener(onClickHeartButton(myFragmentView));

		
		Save = (Button) myFragmentView.findViewById(R.id.Save);
		Save.setOnClickListener(onClickSave(myFragmentView));
		Load = (Button) myFragmentView.findViewById(R.id.btn4);
		Load.setOnClickListener(onClickLoad(myFragmentView));
		//Update=(Button)myFragmentView.findViewById(R.id.btn5);
		//Update.setOnClickListener(onClickUpdate(myFragmentView));
		
		
		firstName=(EditText)myFragmentView.findViewById(R.id.FN);
		name=(EditText)myFragmentView.findViewById(R.id.name);
		patientID=(EditText)myFragmentView.findViewById(R.id.PID);
		minH=(EditText)myFragmentView.findViewById(R.id.Hmin);
		maxH=(EditText)myFragmentView.findViewById(R.id.Hmax);
		minI=(EditText)myFragmentView.findViewById(R.id.Imin);
		maxI=(EditText)myFragmentView.findViewById(R.id.Imax);
		minB=(EditText)myFragmentView.findViewById(R.id.phNumber);
		getMSG=(EditText)myFragmentView.findViewById(R.id.send_message);
		
		ourAlarm=MediaPlayer.create(getActivity(), R.raw.alarm);
		//Declare the timer		

		/**we need to compare the value of indexalarm and getalarm for the sound 
		alarm within SetValue.java fragment actibity if the sensor value croos the
		 threshold level which store in database otherwise the sound won't alarm within setvalue.java. 
		 But it sound when the fragment destroy and again comes to UI**/
		//if (currentPatient.getAlarm()==SettingUpDialogue.indexAlarm){
			                                                         
		//Set the schedule function and rate
		timer1 =new Timer();
		try{
timer1.scheduleAtFixedRate(new TimerTask() {
	
	@Override
	public void run() {
		//Called each time when 1000 milliseconds (1 second) (the period parameter)		
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//We must use this function in order to change and compare constantly the sonsor data to our database value..
			//	heart=FragmentBluetoothMain.heartrate_val;
			  ActionPerformForAlarm();
			  ActionPerformForMessage();
			}

		});
		
	}
}, 
0,//Set how long before to start calling the TimerTask (in milliseconds) 
500);
		}catch(Exception e){
			e.printStackTrace();
			timer1.cancel();
		}

//Set the amount of time between each execution (in milliseconds)
//
//		//}else{
////			Toast.makeText(getActivity(), "Sound Alam is not set", Toast.LENGTH_SHORT).show();
//		//}
//		timer2 =new Timer();
//		timer2.scheduleAtFixedRate(new TimerTask() {
//			
//			@Override
//			public void run() {
//				//Called each time when 1000 milliseconds (1 second) (the period parameter)		
//				getActivity().runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						//We must use this function in order to change and compare constantly the sonsor data to our database value..
//					//	heart=FragmentBluetoothMain.heartrate_val;
//					//   sendSMS();
//					   //sendMsg();
//					   
//					}
//
//				});
//				
//			}
//		}, 
//		0,//Set how long before to start calling the TimerTask (in milliseconds) 
//		500);//Set the amount of time between each execution (in milliseconds)
		
        return myFragmentView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		activity.registerReceiver(messageReceiver, new IntentFilter(BLUETOOTH_UPDATES));
	}
	
	@Override
	public void onDestroy() {
	super.onDestroy();
	activity.unregisterReceiver(messageReceiver);
		timer1.cancel();//in order to solve Timer-0 error..
	//	timer2.cancel();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//------>Process of Saving Database <---------///

	//-------->By clicking Update Button---------<//////
//	private OnClickListener onClickUpdate(View myFragmentView) {
//		return new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//			if(updateData()>0)
//			{
//				Toast.makeText(getActivity(), "Update sucessfully", Toast.LENGTH_SHORT).show();
//			
//			}
//			else
//			{
//				Toast.makeText(getActivity(), "Patient ID does not Exists!", Toast.LENGTH_SHORT).show();
//
//			}				
//			}
//			
//		};
//	}
//-------->By clicking Load Button---------<//////
	public String SendPatientIDToServer(String id) {
		return id;
	}
	private OnClickListener onClickLoad(View myFragmentView) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (patientID.getText().toString().isEmpty()){
					Toast toast=Toast.makeText(getActivity(), "Please Enter PatientID ", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					Log.e("OnclickLoad", "Just a Toast From Load");
				}else{				
					autoSendValidation=true;
				Log.e("onclickedload","Clicking on Load Button");
				
				  populateData();
				firstName.setText(currentPatient.getName());
				name .setText(currentPatient.getFirstName());
		           patientID.setText(currentPatient.getId());
		            minH.setText(currentPatient.getNumHeartRateMin());
		            maxH.setText(currentPatient.getNumHeartRateMax());
		            minI.setText(currentPatient.getNumInterBeatIntervalMin());
		            maxI.setText(currentPatient.getNumInterBeatIntervalMax());
		            minB.setText(currentPatient.getNumBreathingrateMin());
		            getMSG.setText(currentPatient.getNumBreathingrateMax());
		           SettingUpDialogue.indexAlarm=currentPatient.getAlarm();
		           SettingUpDialogue.indexMessage=currentPatient.getMessage();

				
		          
			     }
			   }
			
			
	
		  };
	}
	
	//-------->By clicking Save Button<---------//////
	private OnClickListener onClickSave(final View myFragmentView) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (patientID.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Please Enter PatientID ", Toast.LENGTH_LONG).show();
					Log.e("OnclickSave", "Just a Toast From Save");
				}
				else
				{
				//get the values that we want to save
				nameET=firstName.getText().toString();
				firstNameET=name.getText().toString();
				pIDET=patientID.getText().toString();
				minHET=minH.getText().toString();
			    maxHET= maxH.getText().toString();
				minIET= minI.getText().toString();
				maxIET=maxI.getText().toString();
				minBET=minB.getText().toString();
				maxBET=getMSG.getText().toString();
				alarm=SettingUpDialogue.indexAlarm;
				message=SettingUpDialogue.indexMessage;
				
				
				//set the values that we extracted
			    patient.setName(nameET);
			    patient.setFirstName(firstNameET);
			    patient.setId(pIDET);
			    patient.setNumHeartRateMin(minHET);
			    patient.setNumHeartRateMax(maxHET);
			    patient.setNumInterBeatIntervalMin(minIET);
			    patient.setNumInterBeatIntervalMax(maxIET);
			    patient.setNumBreathingrateMin(minBET);
			    patient.setNumBreathingrateMax(maxBET);
			    patient.setAlarm(alarm);
			    patient.setMessage(message);
			    insertData(patient);
			    if(updateData()>0)
				{
					Toast.makeText(getActivity(), "Update sucessfully", Toast.LENGTH_SHORT).show();
				
				}
				
			  }
			}
  
			
		};
	}
//	public void sendSMS(){
//			//Log.e("SoundAlarm", "Beyond Threshold Values");
//			
//		Runnable runnable= new Runnable() {
//			
//			@Override
//			public void run() {
//				try{
//		 String phoneNo = minB.getText().toString();
//		  
//		  String pid= patientID.getText().toString();
//		  int lengthOfNumIs= phoneNo.length();
//		  
//		  Integer heart= Integer.parseInt(FragmentBluetoothMain.dataBytes[4],16);
//		  String sms = pid + " has Heart Rate = "+  String.valueOf(heart)+ " and Message:- "+getMSG.getText().toString();
//		  if((Integer.parseInt(FragmentBluetoothMain.dataBytes[4],16)<= Integer.parseInt(currentPatient.getNumHeartRateMin())
//						||Integer.parseInt(FragmentBluetoothMain.dataBytes[4],16)>= Integer.parseInt(currentPatient.getNumHeartRateMax())) && currentPatient.getMessage()==true)
//		  if(autoSendValidation &&(lengthOfNumIs==12||lengthOfNumIs==11))
//		  try {
//				SmsManager smsManager = SmsManager.getDefault();
//				smsManager.sendTextMessage(phoneNo, null,sms, null, null);
//				autoSendValidation=false;
//				Toast.makeText(getActivity(), "SMS Sent!",Toast.LENGTH_LONG).show();
//			  } catch (Exception e) {
//				Toast.makeText(getActivity(),
//					"SMS failed, please try again later!",
//					Toast.LENGTH_LONG).show();
//				e.printStackTrace();
//			  }
//				} catch (Exception e){	
//				
//				e.printStackTrace();
//			}
//			}
//			};
//			new Thread(runnable).start();
//	}
	
	/**
	 * This method will activate the Alarm if the sensor value don't fall into the specified region of the patient's database
	 */
	private void ActionPerformForAlarm() {
		//Log.e("SoundAlarm", "Beyond Threshold Values");
		
	Runnable runnable= new Runnable() {
		
		@Override
		public void run() {
			try{
				if(val_heart!=0 && currentPatient.getNumHeartRateMin() !=null && currentPatient.getNumHeartRateMax()!=null )
				while((val_heart<= Integer.parseInt(currentPatient.getNumHeartRateMin())
						||val_heart>= Integer.parseInt(currentPatient.getNumHeartRateMax())) && currentPatient.getAlarm()==true)
				{
				if(!ourAlarm.isPlaying()){
						ourAlarm.start();
						
						//ourAlarm.setLooping(true);//to sound alarm over and over..
						Log.e("heart Rate", String.valueOf(val_heart));
				}				
				}
                       if(ourAlarm.isPlaying()){
						ourAlarm.pause();/**don't use stop() method because we again need to activate the ALARM if
                                           the SENSOR values don't fall onto interval..***/
						Log.e("SoundAlarm", "Alarm out of threshold");
                       }
                       
                       
                }catch (Exception e){	
	
				//e.printStackTrace();
			}
		}
	};
	new Thread(runnable).start(); /****     we have to start another thread because if we don't then the ALARM play over and 
	                                 over and it stuck to main UI and soon our app wll crash  ****/
}
	
	/**
	 * This method will activate the message if the sensor value don't fall into the specified region of the patient's database
	 */
	/**
	 * This method will activate the message if the sensor value don't fall into the specified region of the patient's database
	 */
	private void ActionPerformForMessage() {
		//Log.e("SoundAlarm", "Beyond Threshold Values");
		
	Runnable runnable= new Runnable() {
		
		@Override
		public void run() {
			try{
                       Integer yMin =  Integer.parseInt((GetSensorDataFragment.minYellow));
                       Integer yMax =  Integer.parseInt((GetSensorDataFragment.maxYellow));
                       if((val_heart<= Integer.parseInt(currentPatient.getNumHeartRateMin())
       						||val_heart>= Integer.parseInt(currentPatient.getNumHeartRateMax())) && currentPatient.getMessage()==true)
                       {
                    	 //  sendSMS();
                    	   GetSensorDataFragment.sendMSG="1";
                       }else if((val_heart<= Integer.parseInt(currentPatient.getNumHeartRateMin())+yMin
          						||val_heart>= Integer.parseInt(currentPatient.getNumHeartRateMax())-yMax) && currentPatient.getMessage()==true){
                    	   GetSensorDataFragment.sendMSG="2";
                       }else{
                    	   GetSensorDataFragment.sendMSG="0";
                       }
                       
                }catch (Exception e){	
	
				//e.printStackTrace();
			}
		}
	};
	new Thread(runnable).start(); /****     we have to start another thread because if we don't then the ALARM play over and 
	                                 over and it stuck to main UI and soon our app wll crash  ****/
}
	
///////////////////////////////////
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//------>Process of Setting Up a Dialogue <---------///
	/**
	 Method for creating a dialog box by clicking on action "With" button
	 */
	private void confirmDialog() {
		DialogFragment newFragment= new SettingUpDialogue();
		newFragment.show(getActivity().getFragmentManager(), "GetDialog");
	}
	//-------->By clicking Action Heart Button---------<//////
	private OnClickListener onClickHeartButton(View myFragmentView) {
		return new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					confirmDialog();
					
				}
			};
		}
	


/**
 * Broadcast receiver for getting Real-time data from Sensors	
 */
	private static BroadcastReceiver messageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			int batLvl_val = 0;
			int perfusion_index = 0;
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

						val_SpO2 = Integer.parseInt(dataBytes[5], 16);
						val_heart = Integer.parseInt(
								dataBytes[7].concat(dataBytes[6]), 16);
						perfusion_index = Integer.parseInt(dataBytes[8], 16);

					}


				} else if (persistenses.getDeviceName().matches(
						SensoStrings.DEVICE_EASY_ECG)) {
					val_heart = Integer.parseInt(dataBytes[54], 16);
					}
			}

		}

	};

	
	
}
