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
	
	public void sendCmd(View view)
	{
//		SerialPortUtils.getInstance().sendCmd("ffaa02a401a7");	
		byte[] buffer = new byte[]{(byte) 0xFF,(byte)0xAA,(byte)0x02,(byte)0xA4,(byte)0x01,(byte)0xA7};
		SerialPortUtils.getInstance().sendBuffer(buffer);
	}
	
	public void closeSerial(View view)
	{
		SerialPortUtils.getInstance().closeSerialPort();
	}

	@Override
	public void onDataReceive(byte[] buffer, int size) {
		printArray(buffer, size);
	}


	private void printArray(byte[] buffer, int size) {
		String result = null;
		for (int i = 0; i < size; i++)
		{
			
			if (result != null)
			{
//				result = result + Integer.toHexString(buffer[i]&0xFF) + " ";
				result = result + String.format("%02x", buffer[i]&0xFF) + " ";
			}
			else
			{
				result = String.format("%02x", buffer[i]&0xFF) + " ";
			}
		}
		Log.i("Receive", result);
	}
	
	
}
