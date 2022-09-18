package com.example.dschat.tasks;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;

import com.example.dschat.SharedMemory;
import com.example.dschat.fileutils.FileWriter;
import com.example.dschat.netutils.Message;
import com.example.dschat.netutils.MessageType;
import com.example.dschat.netutils.Tunnel;
import com.example.dschat.structs.Topic;
import com.example.dschat.structs.TopicsForAdapter;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class ListTopics extends Thread {

    private final Handler mHandler;

    public ListTopics(Handler handler) { mHandler = handler; }

    @Override
    public void run()
    {
        int rindex = ThreadLocalRandom.current().nextInt(0, SharedMemory.Brokers.size());
        InetSocketAddress broker = SharedMemory.Brokers.get(rindex);

        //Open tunnel ask for list of topics
        Tunnel t = new Tunnel();
        t.Connect(broker.getHostName(), broker.getPort());
        Message request = new Message(SharedMemory.Username, MessageType.MyTopics);
        Log.d("RESPONSE", "Request");
        t.Send(request);
        Message response = (Message)t.Recieve();
        if(response == null)
            Log.d("RESPONSE", "null");
        t.Close();

        //Transform data
        ArrayList<Topic> topics = (ArrayList<Topic>)response.getContent();
        ArrayList<TopicsForAdapter> items = new ArrayList<>();
        for (Topic topic: topics)
        {
            int index = SharedMemory.Topics.indexOf(topic);
            if(index != -1)
            {
                if(SharedMemory.Topics.get(index).getLastTimestamp() >= topic.getLastTimestamp())
                    items.add(new TopicsForAdapter(topic.getName(), false));
                else
                    items.add(new TopicsForAdapter(topic.getName(), true));
            }
            else
            {
                items.add(new TopicsForAdapter(topic.getName(), true));
                final File filepath = new File(SharedMemory.AppPath, SharedMemory.Username);
                FileWriter.WriteTopic(filepath.getPath(), topic);
                SharedMemory.Topics.add(new Topic(topic.getName(), 0, topic.getSubscribers(), topic.getBroker()));
            }
        }

        //Push data to main thread
        android.os.Message msg = new android.os.Message();
        msg.obj = items;
        mHandler.sendMessage(msg);
    }
}
