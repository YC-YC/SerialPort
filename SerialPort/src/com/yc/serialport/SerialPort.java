package com.yc.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author YC
 * @time 2016-3-7 ����7:45:35
 */
public class SerialPort {

	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;
	
	public SerialPort(File device, int baudrate) throws IOException{
		FileDescriptor mFd = open(device.getAbsolutePath(), baudrate);
		if (mFd == null)
		{
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}
	
	/**
	 * ��ȡ������
	 * @return
	 */
	public InputStream getInputStream() {
		return mFileInputStream;
	}


	/**
	 * ��ȡ�����
	 * @return
	 */
	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}


	private native FileDescriptor open(String path, int baudrate);
	
	/**
	 * �رմ���
	 * @return
	 */
	public native int close();
	
	static {
		System.loadLibrary("SerialPort");
	}
}
