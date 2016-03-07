package com.yc.serialport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ���Դ���
 * @author YC
 * @time 2016-3-7 ����8:31:49
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
	 * ��ʼ��������Ϣ
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
			// TODO �Զ����ɵ� catch ��
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
		try {
			if (mOutputStream != null)
			{
				mOutputStream.write(buffer);
				result = true;
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return result;
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