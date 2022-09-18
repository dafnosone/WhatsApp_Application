package com.example.dschat.netutils;

import java.io.EOFException;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Tunnel {
	private Socket mSocket ;
	
	ObjectInputStream mObjectInStream;
	ObjectOutputStream mObjectOutStream;
	

	public Tunnel() {}

	public Tunnel(Socket sock)
	{
		mSocket = sock;
		try {
			mObjectOutStream = new ObjectOutputStream(mSocket.getOutputStream());
			mObjectInStream = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Connect(String address , int port )
	{
		try {
			mSocket = new Socket(address, port);
			mObjectOutStream = new ObjectOutputStream(mSocket.getOutputStream());
			mObjectInStream = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Send(Object data )
	{
		try {
			
			mObjectOutStream.writeObject(data);
			mObjectOutStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object Recieve() 
	{
		Object data = null;
		try { data = mObjectInStream.readObject(); }
		catch (SocketTimeoutException | SocketException | EOFException e) { return null; }
		catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
		return data;
	}
	
	public void Close() {
		try {
			mObjectOutStream.close();
			mObjectInStream.close();
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void SetTimeout(int timeout) { try { mSocket.setSoTimeout(timeout); } catch (SocketException e) { e.printStackTrace(); } }


}
