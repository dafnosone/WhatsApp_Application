package com.example.dschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dschat.adapters.TopicsAdapter;
import com.example.dschat.netutils.MessageType;
import com.example.dschat.netutils.Tunnel;
import com.example.dschat.structs.TopicsForAdapter;
import com.example.dschat.tasks.ListTopics;
import com.example.dschat.netutils.Message;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class SelectTopicActivity extends AppCompatActivity {

    public static final String FIRST_BROKER_IP_ADDRESS = "192.168.1.6";

    //UI References
    private RecyclerView mTopicsView;
    private Button btnCreateTopic;

    //Other variables
    private TopicsAdapter mAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);

        //Get UI References
        mTopicsView = findViewById(R.id.recViewTopics);
        btnCreateTopic = findViewById(R.id.btnCreateTopic);
        btnCreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTopicActivity.this, CreateTopicActivity.class);
                startActivity(intent);
            }
        });
        //Create adapter
        mAdapter = new TopicsAdapter(SelectTopicActivity.this);
        mTopicsView.setAdapter(mAdapter);
        mTopicsView.setLayoutManager(new LinearLayoutManager(this));
        //Start handler and thread
        mHandler = new UpdateList(Looper.myLooper());
        ListBrokers();
    }

    private void ListBrokers()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message request = new Message(SharedMemory.Username, MessageType.ListOfBrokers);
                Tunnel tunnel = new Tunnel();
                tunnel.Connect(FIRST_BROKER_IP_ADDRESS, 55555);
                tunnel.Send(request);

                Message response = (Message) tunnel.Recieve();
                SharedMemory.Brokers = (ArrayList<InetSocketAddress>)response.getContent();
                tunnel.Close();


                Thread th = new ListTopics(mHandler);
                th.start();
            }
        });
        thread.start();
    }

    class UpdateList extends Handler {
        public UpdateList(Looper looper) { super(looper); }
        @Override
        public void handleMessage(android.os.Message msg)
        {
            ArrayList<TopicsForAdapter> items = (ArrayList<TopicsForAdapter>)msg.obj;
            mAdapter.setItems(items);
        }
    }
}