package com.example.dschat.fileutils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import com.example.dschat.netutils.Message;
import com.example.dschat.structs.Topic;

public class FileReader {
	
	private String mPath;
	private FileInputStream mInput;
	private int mFileSize;
	
	public FileReader(String path)
	{
		mPath = path;
		try {
			File file = new File(mPath);
			mFileSize = (int)file.length();
			mInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] Read(int length)
	{
		byte[] data = null;
		
		try {
			byte[] raw = new byte[length];
			int bytes = mInput.read(raw);
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
	
	/**
	 * Only ASCII!!!!!!!!!!!!
	 */
	public static Topic ReadTopic(String filepath)
	{
		String topicname = filepath.substring(filepath.lastIndexOf("/")+1);
		topicname = topicname.substring(topicname.lastIndexOf("\\")+1);   //??
		Topic topic = null;
		BufferedReader br;
		try {
			topic = new Topic(topicname); 
			br = new BufferedReader(new java.io.FileReader(filepath));

			Queue<Message> messages = new ArrayDeque<>();
			ArrayList<String> subs = new ArrayList<>();
			String line;
			while((line=br.readLine())!=null)  
			{
				if(line.length() == 0 || line.compareTo("") == 0 || line.compareTo("\n") == 0 || line.compareTo("\r\n") == 0)
					continue;
				if(line.substring(0,5).compareTo("sub: ") == 0)
					subs.add(line.substring(5));
				else if(line.substring(0,5).compareTo("msg: ") == 0)
				{
					Message msg = Message.Load(line.substring(5));
					messages.add(msg);
				}		
			}
			topic.setMessages(messages);
			topic.setLastTimestamp(messages.size());
			topic.setSubscribers(subs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  //creates a buffering character input stream  
		catch (IOException e) {
			e.printStackTrace();
		}
		return topic;
	}

}
