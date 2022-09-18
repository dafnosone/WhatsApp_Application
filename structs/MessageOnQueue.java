package com.example.dschat.structs;

import java.util.ArrayList;

import com.example.dschat.netutils.Message;

public class MessageOnQueue {
	public Message msg;
	public ArrayList<String> Subscribers;
	
	public MessageOnQueue(Message message, ArrayList<String> subscribers)
	{
		msg = message;
		Subscribers = subscribers;
	}
	
}
