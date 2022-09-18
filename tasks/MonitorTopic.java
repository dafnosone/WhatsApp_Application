package com.example.dschat.tasks;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.dschat.SharedMemory;
import com.example.dschat.fileutils.FileWriter;
import com.example.dschat.netutils.Message;
import com.example.dschat.netutils.MessageType;
import com.example.dschat.netutils.Tunnel;
import com.example.dschat.structs.Media;
import com.example.dschat.structs.Topic;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTopic extends Thread
{
    private String mTopicname;
    private long mLastTimestamp;
    private InetSocketAddress mBroker;
    private Handler mHandler;
    private Context mContex;
    private Tunnel mTunnel;

    private volatile boolean mRunning = false;

    public MonitorTopic(Context context, String topic, long timestamp, InetSocketAddress broker, Handler handler)
    {
        mTopicname = topic;
        mLastTimestamp = timestamp;
        mBroker = broker;
        mHandler = handler;
        mContex = context;
    }

    @Override
    public void run()
    {
        mRunning = true;
        mTunnel = new Tunnel();
        mTunnel.Connect(mBroker.getHostName(), mBroker.getPort());
        mTunnel.SetTimeout(500);
        Message request = new Message(SharedMemory.Username, mTopicname, MessageType.NotifyForTopic, (Long)mLastTimestamp);
        mTunnel.Send(request);

        while(mRunning)
        {
            Message response = (Message)mTunnel.Recieve();
            if(response == null)
                continue;
            Log.d("Consumer", response.toString());
            mTunnel.Send(MessageType.OK);
            final String filepath = SharedMemory.AppPath+"/"+SharedMemory.Username+"/"+mTopicname;
            FileWriter.appendToTopic(filepath, response);
            Topic topic = SharedMemory.Topics.get(SharedMemory.Topics.indexOf(new Topic(mTopicname)));
            topic.getMessages().add(response);
            topic.setLastTimestamp(topic.getLastTimestamp()+1);
            if(response.getType() == MessageType.Photo || response.getType() == MessageType.Video)
                PullMedia(response);
            else
                NotifyMainThread(response);
        }
        mTunnel.Close();
        Log.d("Consumer", "Done");
    }

    private void PullMedia(Message chunk)
    {
        File file = new File(mContex.getExternalFilesDir(null), SharedMemory.Username+"/media");
        Media media = (Media) chunk.getContent();
        FileWriter writer = new FileWriter(file.getPath()+"/"+media.ID+media.Extension);
        writer.Append(media.Chunck);
        NotifyMainThread(chunk);

        long left = media.Size - media.Chunck.length;
        while(left > 0)
        {
            chunk = (Message) mTunnel.Recieve();
            mTunnel.Send(MessageType.OK);
            media = (Media) chunk.getContent();
            writer.Append(media.Chunck);
            left -= media.Chunck.length;
        }

        if(chunk.getType() == MessageType.Video || chunk.getType() == MessageType.Photo)
            NotifyMainThread(chunk);
    }

    private void NotifyMainThread(Message object)
    {
        android.os.Message msg = new android.os.Message();
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

    public void Terminate() { mRunning = false; }


}
