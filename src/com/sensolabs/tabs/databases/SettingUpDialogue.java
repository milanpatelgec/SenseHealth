package com.sensolabs.tabs.databases;


import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bleexample.R;
//import com.example.cardioapp.bluetooth.FragmentBluetoothGraph;
//import com.example.cardioapp.database.SetValue;

public class SettingUpDialogue extends DialogFragment{
	Patient patient= new Patient();
	MediaPlayer ourAlarm;
public static boolean indexAlarm=false;
public static boolean indexMessage=false;
//public  void soundAlarm() ;
	// boolean[] saveChecked= new boolean [index];


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final String[] select=new String[]{"Sound Alarm","Send Message"};
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 ourAlarm=MediaPlayer.create(getActivity(), R.raw.alarm);
		 
		 //The column name on the cursor containing the string to display in the label.
		 builder.setTitle("Pick Up Your Action ").setMultiChoiceItems( select,new boolean[]{indexAlarm,indexMessage},new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
	
					
					if(which==0){ //0  refers to "Sound Alarm", 1 refers to "Send Message"
					//	 saveChecked[0]=true;
					indexAlarm=isChecked;
					patient.setAlarm(indexAlarm);
					try {
						ourAlarm.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					if (isChecked==true )
					{
					  
					  ourAlarm.start();
						
					  Toast.makeText(getActivity(), "Sound Alarm Active",Toast.LENGTH_SHORT ).show();
			
					}
					else{
						if(ourAlarm.isPlaying())
						{
							ourAlarm.stop();
						}
							
						Toast.makeText(getActivity(), "Sound Alarm Deactivate",Toast.LENGTH_SHORT ).show();
						
						}
					}
					if(which==1){
						indexMessage=isChecked;
						patient.setMessage(indexMessage);
						if (isChecked==true)
							Toast.makeText(getActivity(), "Send Message Active",Toast.LENGTH_SHORT ).show();
						else
							Toast.makeText(getActivity(), "Send Message Deactivate",Toast.LENGTH_SHORT ).show();
						
						}
					

				}

			
		});
		 
		///Set the action Buttons
		
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
			//ourAlarm.reset();
				
			//	Toast.makeText(getActivity(), "You clicked on OK button",Toast.LENGTH_SHORT ).show();
				
					Log.e("DialogBox", "isn't playing");
			}

			
		});
		
	builder	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				  
				//Toast.makeText(getActivity(), "You clicked on Cancel button",Toast.LENGTH_SHORT ).show();
			}
		});
		
		return builder.create();
	}
 


	/*  this method is called when dialog is sudden remove without pressing cancel button
	 */
	@Override
	public void onDestroyView() {
		Log.e("onDestroyView", "onDestroyView");
		super.onDestroyView();
		if(ourAlarm.isPlaying())
			ourAlarm.release();
		else
			Log.e("onDestroyView", "onDestroknsksdnfsyView");
	}


	
	
}