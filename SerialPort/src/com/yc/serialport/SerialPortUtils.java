package com.yc.serialport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

/**
 * 测试串口
 * @author YC
 * @time 2016-3-7 下午8:31:49
 */
public class SerialPortUtils {
	
	private static final String PATH = "/dev/ttyS0";  
    private static final int BAUDRATE = 115200;
	
	private static SerialPortUtils protUtils;
	
	private SerialPort mSerialPort = null;
	private InputStream mInputStream;
	private OutputStream mOutputStream;
	private OnDataReceiveListener mDataReceiveListener = null;  
	
	private ReadThread mReadThread;
	private boolean mIsReadThreadStop = false;
	
	
	public interface OnDataReceiveListener {  
        public void onDataReceive(byte[] buffer, int size);  
    }  
  
    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {  
    	mDataReceiveListener = dataReceiveListener;  
    } 
	
	public static SerialPortUtils getInstance()
	{
		if (protUtils == null)
		{
			protUtils = new SerialPortUtils();
		}
		
		return protUtils;
	}
	
	/**
	 * 初始化串口信息
	 */
	public void openSerialPort()
	{
		try {
			mSerialPort = new SerialPort(new File(PATH), BAUDRATE);
			mInputStream = mSerialPort.getInputStream();
			mOutputStream = mSerialPort.getOutputStream();
			
			mReadThread = new ReadThread();
			mIsReadThreadStop = false;
			mReadThread.start();
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void closeSerialPort()
	{
		mIsReadThreadStop = true;
		if (mReadThread != null)
		{
			mReadThread.interrupt();
		}
		if (mSerialPort != null)
		{
			mSerialPort.close();
		}
	}
	
	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean sendCmd(String cmd)
	{
		byte[] buffer = cmd.getBytes();
		return sendBuffer(buffer);
	}
	
	public boolean sendBuffer(byte[] buffer)
	{
		boolean result = false;
		printArray(buffer, buffer.length);
		try {
			if (mOutputStream != null)
			{
				mOutputStream.write(buffer);
				result = true;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return result;
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
		Log.i("Send", result);
	}
	
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!mIsReadThreadStop && !isInterrupted())
			{
				try {
					int size;
					if (mInputStream != null)
					{
						byte[] buffer = new byte[256];
						size = mInputStream.read(buffer, 0, 256);
						if (size > 0)
						{
							if (mDataReceiveListener != null)
							{
								mDataReceiveListener.onDataReceive(buffer, size);
							}
						}
					}
					Thread.sleep(10);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
