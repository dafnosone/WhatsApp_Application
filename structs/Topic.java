package com.example.dschat.structs;


import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;


import com.example.dschat.netutils.Message;
import com.example.dschat.netutils.MessageType;

public class Topic implements Serializable {
	private String name;
	private Queue<Message> messages = new ArrayDeque<>();
	private ArrayList<String> subscribers = new ArrayList<>();
	private long lastTimestamp;
	private InetSocketAddress broker;

	public Topic(String name) {
		this.name = name;
	}

	public Topic(String name, ArrayList<String> subs)
	{
		this.name = name;
		subscribers = subs;
	}

	public Topic(String name, Queue<Message> messages, ArrayList<String> subscribers, int lastTimestamp )
	{
		this.lastTimestamp = lastTimestamp;
		this.name = name;
		this.messages = messages;
		this.subscribers = subscribers;
	}

	public Topic(String name, long lastTimestamp, ArrayList<String> subs,InetSocketAddress broker)
	{
		this.name = name;
		this.lastTimestamp = lastTimestamp;
		subscribers = subs;
		this.broker = broker;
	}

	public Topic(String name, long lastTimestamp)
	{
		this.name = name;
		this.lastTimestamp = lastTimestamp;
	}

	public Topic(String name, Message msg, ArrayList<String> subscribers, int lastTimestamp)
	{
		this.lastTimestamp = lastTimestamp;
		this.name = name;
		messages = new ArrayDeque<>();
		messages.add(msg);
		this.subscribers = subscribers;
	}

	public MessageOnQueue addMessage(String username, String topicname, MessageType type, String data)
	{
		Message msg = new Message(username, topicname, ++lastTimestamp, type, data);
		messages.add(msg);
		ArrayList<String> subs = new ArrayList<>();
		for(String sub : subscribers)
			subs.add(sub);
		return new MessageOnQueue(msg, subs);
	}

	public MessageOnQueue addMessage(String username, String topicname, MessageType type, Media data)
	{
		Message msg = new Message(username, topicname, ++lastTimestamp, type, new Media(data.ID, data.ChuncksNumber, data.Size, data.Extension));
		messages.add(msg);
		ArrayList<String> subs = new ArrayList<>();
		for(String sub : subscribers)
			subs.add(sub);
		return new MessageOnQueue(new Message(username, topicname, lastTimestamp, type, data), subs);
	}

	public InetSocketAddress getBroker() {
		return broker;
	}

	public void setBroker(InetSocketAddress broker) {
		this.broker = broker;
	}

	public long getLastTimestamp(){return lastTimestamp;}

	public void setLastTimestamp(long timestamp){lastTimestamp = timestamp;}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Queue<Message> getMessages() {
		return messages;
	}

	public void setMessages(Queue<Message> messages) {
		this.messages = messages;
	}

	public ArrayList<String> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(ArrayList<String> subscribers) {
		this.subscribers = subscribers;
	}

	public boolean isUpToDate(Topic other)
	{
		return lastTimestamp == other.lastTimestamp;
	}

	@Override
	public boolean equals(Object other)
	{
		if(this == other)
			return true;
		if(!(other instanceof Topic))
			return false;
		Topic topic = (Topic)other;
		if(topic.name.compareTo(this.name) != 0)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		String body = "";
		for(String sub : subscribers)
			body += "sub: "+ sub + "\n";
		for(Message msg: messages)
			body +="msg: "+ msg.toString() + "\n";

		return body;
	}

}
