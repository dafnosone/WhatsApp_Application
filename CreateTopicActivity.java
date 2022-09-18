package com.example.dschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dschat.netutils.Message;
import com.example.dschat.netutils.MessageType;
import com.example.dschat.netutils.Tunnel;
import com.example.dschat.structs.Topic;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class CreateTopicActivity extends AppCompatActivity {

    private Button btnDone, btnAdd;
    private EditText userToAdd, topicNameToAdd;
    private boolean done = false;
    private ArrayList<String> subs;
    private String newTopicName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        btnAdd = findViewById(R.id.btnAdd);
        btnDone = findViewById(R.id.btnDone);
        userToAdd = findViewById(R.id.edtTxtUserToAdd);
        topicNameToAdd = findViewById(R.id.edtTxtTopicName);

        subs = new ArrayList<>();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = userToAdd.getText().toString();
                if (input == null || input.length() == 0)
                {
                    Toast.makeText(CreateTopicActivity.this, "Insert a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                subs.add(input);
                userToAdd.setText("");
            }

        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Tunnel t = new Tunnel();
                        newTopicName = topicNameToAdd.getText().toString();
                        InetSocketAddress broker = SharedMemory.SelectBroker(newTopicName);
                        t.Connect(broker.getHostName(), broker.getPort());
                        Message req = new Message(SharedMemory.Username,newTopicName, MessageType.CreateNewTopic,subs);
                        t.Send(req);
                        t.Close();
                        Topic topic = new Topic(newTopicName, 0, subs, broker);
                        SharedMemory.Topics.add(topic);
                        Intent intent = new Intent(CreateTopicActivity.this, SelectTopicActivity.class);
                        startActivity(intent);
                    }
                });
                thread.start();
            }
        });

    }
}