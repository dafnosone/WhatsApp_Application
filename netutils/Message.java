package com.example.dschat.netutils;

import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

import com.example.dschat.structs.Media;

public class Message implements Serializable {
	public static int KiB = 1024;
	public static int MiB = 1024 * KiB;
	public static int GiB = 1024 * MiB;

	private String username;
	private String topic = "Undefined";
	private long timestamp = 0;
	private Date date;
	private MessageType type;
	private Object content = null;

	public Message(String name, String topic, long timestamp, Date date, MessageType type, Object data )
	{
		this.topic = topic;
		username = name;
		this.timestamp = timestamp;
		this.date = date;
		this.type = type;
		content = data;
	}

	public Message(String name, String topic, Date date, MessageType type, Object data )
	{
		this.topic = topic;
		username = name;
		this.date = date;
		this.type = type;
		content = data;
	}

	public Message(String name, String topic, long timestamp, MessageType type, Object data )
	{
		username = name;
		this.topic = topic;
		this.timestamp = timestamp;
		date = new Date(System.currentTimeMillis());
		this.type = type;
		content = data;
	}

	public Message(String name, String topic, MessageType type, Object data )
	{
		username = name;
		this.topic = topic;
		date = new Date(System.currentTimeMillis());
		this.type = type;
		content = data;
	}

	public Message(String name, MessageType type, Object data)
	{
		username = name;
		date = new Date(System.currentTimeMillis());
		this.type = type;
		content = data;
	}

	public Message(String name, MessageType type) {
		username =name;
		date = new Date(System.currentTimeMillis());
		this.type = type;
	}

	public Message(String name, long timestamp, Date date, MessageType type, Object data)
	{
		username = name;
		this.timestamp = timestamp;
		this.date = date;
		this.type = type;
		content = data;
	}

	@Override
	public String toString()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String data = username + ", " + timestamp + ", " + formatter.format(date) + ", " + type.toString() + ", ";
		switch(type) {
			case Text:
				data += (String)content;
				break;
			case Video:
			case Photo:
				data += ((Media)content).toString();
				break;
			default:
				return null;
		}
		return data;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}

	public static Message Load(String row)
	{
		StringTokenizer tokenizer = new StringTokenizer(row, ", ");
		String username = tokenizer.nextToken();
		long timestamp = Long.parseLong(tokenizer.nextToken());
		Date rawdate = null;
		try {rawdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(tokenizer.nextToken()); } catch (ParseException e) { e.printStackTrace(); }
		MessageType type = MessageType.valueOf(tokenizer.nextToken());
		if(type == MessageType.Text)
		{
			String data = tokenizer.nextToken();
			while(tokenizer.hasMoreTokens())
				data += " " + tokenizer.nextToken();
			Message msg = new Message(username, timestamp, rawdate, type, data);
			return msg;
		}
		else
		{
			String data = tokenizer.nextToken();
			UUID id = UUID.fromString(data);
			data = tokenizer.nextToken();
			int size = Integer.parseInt(data);
			final int chuncks = size / MiB + (size % MiB != 0 ? 1 : 0);
			String extension = tokenizer.nextToken();
			Media media = new Media(id, chuncks, size, extension);
			return new Message(username, timestamp, rawdate, type, media);
		}
	}
}
