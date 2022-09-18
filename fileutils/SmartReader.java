package com.example.dschat.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SmartReader {
	private String mPath;
	private FileInputStream mInput;
	private int mFileSize;
	
	public SmartReader(String path)
	{
		mPath = path;
		File file = new File(mPath);
		mFileSize = (int)file.length();
	}
	
	public byte[] Read(int length, long offset)
	{
		byte[] data = null;
		
		try {
			mInput = new FileInputStream(mPath);
			mInput.getChannel().position(offset);
			byte[] raw = new byte[length];
			int bytes = mInput.read(raw);
			mInput.close();
			if (bytes == length)
				return raw;
			else if (bytes>0)
			{
				data = new byte[raw.length];
				for (int i = 0; i<bytes;i++)
					data[i] = raw[i];
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public void Close() {
		try {
			mInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int GetFileSize() { return mFileSize; }
}
