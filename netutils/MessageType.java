package com.example.dschat.netutils;

public enum MessageType {
	NotifyOtherBrokersNewMsg,
	NotifyOtherBrokersNewTopic,
	CreateNewTopic,
	OK,
	Undefined,
	Text,
	Photo,
	Video,
	ListOfBrokers,//Basically producer asks to know for all the brokers
	MyTopics,//Basically consumer asks for brokers for his topics
	NotifyForTopic//Consumer request for message history in a topic
}