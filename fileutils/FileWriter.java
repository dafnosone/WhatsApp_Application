package com.example.dschat.fileutils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.dschat.netutils.Message;
import com.example.dschat.structs.Topic;

public class FileWriter {
	
	private String mPath;
	
	
	public FileWriter(String path)
	{
		mPath = path;
		try {
			FileOutputStream out = new FileOutputStream(mPath);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Append(byte [] data)
	{
		try {
			FileOutputStream out = new FileOutputStream(mPath, true);
			out.write(data);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Only ASCII please!!!!!!!!!
	 * @param filepath
	 * @param topic
	 */
	public static void WriteTopic(String filepath, Topic topic)
	{
		try {
			FileOutputStream out = new FileOutputStream(filepath+"/"+topic.getName());
			out.write(topic.toString().getBytes());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void appendToTopic(String filepath, Message msg) {
		try {
			FileOutputStream out = new FileOutputStream(filepath,true);
			String row = "msg: "+ msg.toString()+"\n";
			out.write(row.getBytes());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
