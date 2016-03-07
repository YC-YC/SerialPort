package com.yc.serialport;

import com.yc.serialport.SerialPortUtils.OnDataReceiveListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements OnDataReceiveListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	public void openSerial(View view)
	{
		SerialPortUtils.getInstance().openSerialPort();
		SerialPortUtils.getInstance().setOnDataReceiveListener(this);
	}
	
	public void closeSerial(View view)
	{
		SerialPortUtils.getInstance().closeSerialPort();
	}


	@Override
	public void onDataReceive(byte[] buffer, int size) {
		Log.i("Receive", buffer.toString());
	}
	
}
