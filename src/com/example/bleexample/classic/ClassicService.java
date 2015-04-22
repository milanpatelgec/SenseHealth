package com.example.bleexample.classic;

import static com.example.bleexample.classic.IntentIdentifier.BLUETOOTH_UPDATES;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.bleexample.SampleGattAttributes;
import com.sensolabs.tabs.classes.Persistenses;
import com.sensolabs.tabs.classes.SensoStrings;

public class ClassicService extends Service {
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();
	private static String TAG = ClassicService.class.getSimpleName();
	// Random number generator
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket mBluetoothSocket = null;
	private BluetoothDevice mBluetoothDevice = null;
	private OutputStream mOutputStream = null;
	private InputStream mInputStream = null;
	public final static UUID UUID_PULSE_RATE_MEASUREMENT = UUID
			.fromString(SampleGattAttributes.PULSE_RATE_MEASURET);
	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
			.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);


	private Thread workerThread;
	private byte[] readBuffer;
	ArrayList<Byte> receivedBytes;
	private int readBufferPosition;
	UUID uuid;
	private volatile boolean stopWorker;
	private int counter = 0;
	private static String dataType = "data";
	private static String waveType = "wave";
	private static String ECGType = "ECG";
	private String deviceName = "";

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public ClassicService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return ClassicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/** method for clients */
	// public int getRandomNumber() {
	// return mGenerator.nextInt(100);
	// }

	public boolean setBtDeviceByAdress(String deviceAdress) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(deviceAdress);
		return true;
	}

	public boolean connect() throws Throwable {

		if (null == mBluetoothDevice) {
			throw new IllegalStateException("No device set");
			
		}
		
		Log.d("BT Address", String.valueOf(mBluetoothDevice.getAddress()));
		
		if (Build.VERSION.SDK_INT >= 10) {
			// insecure Socket
			mBluetoothSocket = mBluetoothDevice
					.createInsecureRfcommSocketToServiceRecord(UUID_PULSE_RATE_MEASUREMENT);
		} else {
			// secure Socket
			mBluetoothSocket = mBluetoothDevice
					.createRfcommSocketToServiceRecord(UUID_PULSE_RATE_MEASUREMENT);
		}
		Log.d("BT", String.valueOf(mBluetoothSocket.isConnected()));
		
		if (mBluetoothSocket.isConnected()){
			return true;
			}
		else{
			mBluetoothSocket.connect();
			return true;
			}

	}

	private InputStream getInputStream() throws IOException {
		mInputStream = mBluetoothSocket.getInputStream();
		return mInputStream;

	}

	private OutputStream getOutputStream() throws IOException {
		mOutputStream = mBluetoothSocket.getOutputStream();
		return mOutputStream;

	}

	public boolean beginListenForData() {
		if (mInputStream == null) {
			try {
				getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		receivedBytes = new ArrayList<Byte>();
		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[4000];
		final int counterACK = 0;

		final byte[] buffer = new byte[1024];

		workerThread = new Thread(new Runnable() {

			@SuppressWarnings("unused")
			@Override
			public void run() {
				int bytes = 0;
				int avaiableBytes = 0;
				int avaiableBytesECG = 0;

				// i = mInputStream.read(buffer);
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						avaiableBytes = mInputStream.available();
					} catch (IOException e1) {
						Log.e("", "Error: " + e1.getClass());
						e1.printStackTrace();
					}

					// if(bytesAvailable ==18){
					// sendCommandVersionInfo("A5", "55","01" , "00","00");}
					if (avaiableBytes > 0) {
						byte[] packetBytes = new byte[avaiableBytes];
						try {
							avaiableBytesECG = mInputStream.read(packetBytes);
						} catch (IOException e1) {
							Log.e("", "Error: " + e1.getClass());
							e1.printStackTrace();
						}
						if (getDeviceName().matches(
								SensoStrings.DEVICE_PULSE_OXIMETER)) {
							proceedOnPulseOxie(avaiableBytes, packetBytes);

						} else if (getDeviceName().matches(
								SensoStrings.DEVICE_EASY_ECG)) {
							proceedOnECG(avaiableBytesECG, packetBytes);
						} else {
							Log.e(TAG, "Wrogggg");
						}

					}

				}
			}

		});

		workerThread.start();
		return true;
	}

	public void putDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	private String getDeviceName() {
		return this.deviceName;
	}

	private void proceedOnECG(int avaiableBytesECG, byte[] packetBytes) {
		if (avaiableBytesECG == 18) {
			sendCommandVersionInfo("A5", "55", "01", "00", "00");
		}

		if (avaiableBytesECG > 0 && avaiableBytesECG == 58) {
			if (avaiableBytesECG == 58) {
				proceedOnDataPacket(avaiableBytesECG, getECG(), packetBytes);
			}

		}
	}

	private void proceedOnPulseOxie(int avaiableBytes, byte[] packedBytes) {

		if (avaiableBytes != 7 || avaiableBytes == 11 || avaiableBytes == 12)
			if (avaiableBytes > 0) {
				if (avaiableBytes == 11) {
					proceedOnDataPacket(avaiableBytes, getWave(), packedBytes);
				} else if (avaiableBytes == 12) {
					proceedOnDataPacket(avaiableBytes, getData(), packedBytes);
				} else {
					Log.d(TAG, "there is something wrong with Pulse Ox");
				}

			}
	}

	private void proceedOnDataPacket(int avaiableBytes, String dataIdentifier,
			byte[] packetBytes) {

		int bufferPosition = 0;
		byte[] buffers = new byte[1024];
		ArrayList<Byte> getBytes = new ArrayList<Byte>();

		for (int i = 0; i < avaiableBytes; i++) {
			byte b = packetBytes[i];
			getBytes.add(b);
			counter++;

			int typeWave = 0, typeData = 0, typeECG = 0;
			if (getWave().equals(dataIdentifier)) {
				typeWave = 11;
			}

			if (getData().equals(dataIdentifier)) {
				typeData = 12;
			}

			if (getECG().equals(dataIdentifier)) {
				typeECG = 58;
			}
			//
			if ((counter == typeWave && getWave().equals(dataIdentifier))
					|| (getData().equals(dataIdentifier) && counter == typeData)
					|| (counter == typeECG && getECG().equals(dataIdentifier))) {

				final byte[] encodedBytes = new byte[getBytes.size()];
				for (int index = 0; index < getBytes.size(); index++) {
					encodedBytes[index] = getBytes.get(index);
					// Log.e("Received Bytes",
					// receivedBytes.get(index).toString());
				}
				onDataReceived(encodedBytes, avaiableBytes);
				getBytes = new ArrayList<Byte>();

				counter = 0;

				bufferPosition = 0;

			} else {
				buffers[bufferPosition++] = b;
			}
		}

	}

	public void onDataReceived(byte[] bytes, int bytesavaiable) {

		String message = byteArrayToHexString(bytes);

		if (message != null && bytesavaiable != 0) {
			Intent intent = new Intent(BLUETOOTH_UPDATES);
			intent.putExtra(SensoStrings.DATA_RECEIVED_INTENT, message);
			intent.putExtra(SensoStrings.BYTE_RECEIVED_INTENT, bytesavaiable);
			sendBroadcast(intent);
		}

	}

	public boolean writeBytes(byte[] bytes) {

		if (mBluetoothSocket.isConnected()) {
			try {
				if (mOutputStream == null) {
					getOutputStream();
				}

				if (null == bytes) {
					return false;
				}

				mOutputStream.write(bytes);
				mOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("Bluetooth", "Error by sending Bytes: " + e.getClass());
				return false;
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Bluetooth Socket might be closed", Toast.LENGTH_SHORT)
					.show();
		}
		return true;
	}

	public boolean closeBT() {
		stopWorker = true;
		try {
			mOutputStream.close();
			mInputStream.close();
			mBluetoothSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			 mBluetoothAdapter = null;
			 mBluetoothSocket = null;
			mBluetoothDevice = null;
			 mOutputStream = null;
			mInputStream = null;
			Log.e("Bluetooth",
					"Error while closing connection: " + e.getClass());
			return false;
		}
		Log.d("Bluetooth", "closed");
		return true;
	}

	public boolean checkConnection() {
		return mBluetoothSocket.isConnected();
	}

	public void setData(String data) {
		ClassicService.dataType = data;
	}

	public void setWave(String wave) {
		ClassicService.waveType = wave;
	}

	private String getData() {
		return ClassicService.dataType;
	}

	private String getWave() {
		return ClassicService.waveType;
	}

	public void setECG(String ECG) {
		ClassicService.ECGType = ECG;
	}

	private String getECG() {
		return ClassicService.ECGType;
	}

	public void sendCommandVersionInfo(String s1, String s2, String s3,
			String s4, String s5) {
		String pa1 = s1;
		String pa2 = s2;
		String cmd = s3;
		String p0 = s4;
		String p1 = s5;
		String command = pa1 + pa2 + cmd + p0 + p1;
		byte[] addrArray = hexStringToByteArray(command);

		if (getDeviceName().matches(SensoStrings.DEVICE_EASY_ECG)) {
			getCRC(addrArray, addrArray.length);
		}
		writeBytes(addrArray);
	}

	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		// Log.e("data", String.valueOf(data));
		return data;
	}

	public static String byteArrayToHexString(byte[] array) {
		StringBuffer hexString = new StringBuffer();
		for (byte b : array) {
			int intVal = b & 0xff;
			if (intVal < 0x10)
				hexString.append("0");
			hexString.append(Integer.toHexString(intVal) + "x");
		}
		return hexString.toString();
	}

	public void sendInquire() {
		try {
			SendCommand(SensoStrings._command_inquire);
			return;
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	public void SendCommand(byte abyte0[]) throws IOException {
		getCRC(abyte0, abyte0.length);
		writeBytes(abyte0);
		// readData();
	}

	public static void getCRC(byte abyte0[], int i) {
		byte byte0 = 0;
		int j = 0;
		do {
			if (j >= i - 1) {
				abyte0[j] = byte0;
				return;
			}
			byte0 = SensoStrings.crc_table[0xff & (byte0 ^ abyte0[j])];
			j++;
		} while (true);
	}

	// //////////////////*****************************************///////////////////////////////////////

}