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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dschat.adapters.MessageAdapter;
import com.example.dschat.fileutils.FileReader;
import com.example.dschat.netutils.Message;
import com.example.dschat.netutils.MessageType;
import com.example.dschat.netutils.Tunnel;
import com.example.dschat.structs.Media;
import com.example.dschat.structs.Topic;
import com.example.dschat.tasks.MonitorTopic;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;

public class MessageViewActivity extends AppCompatActivity {

    //UI References
    private EditText mInputMessage;

    //Other Variables
    private String mTopic;
    private MessageAdapter mAdapter;
    private MessageHandler mHandler;

    private MonitorTopic mThread = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        RecyclerView recyclerView = findViewById(R.id.recViewHistory);
        mInputMessage = findViewById(R.id.textViewMessage);
        SetupUI();

        Intent intent = getIntent();
        mTopic = intent.getStringExtra("topic");

        mAdapter = new MessageAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHandler = new MessageHandler(Looper.myLooper());

        //Populate array
        Topic topic = SharedMemory.Topics.get(SharedMemory.Topics.indexOf(new Topic(mTopic)));
        ArrayList<Message> history = new ArrayList<>();
        for (Message msg : topic.getMessages()) {
            if(msg.getType() == MessageType.Video)
                ((Media)msg.getContent()).Current = 1;
            history.add(msg);
        }
        mAdapter.setHistory(history);


        mThread = new MonitorTopic(this, mTopic, topic.getLastTimestamp(), SharedMemory.SelectBroker(mTopic), mHandler);
        mThread.start();
    }

    public class MessageHandler extends Handler {
        MessageHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message msg)
        {
            Message data = (Message) msg.obj;
            if(data.getType() == MessageType.Photo || data.getType() == MessageType.Video)
            {
                Media media = (Media)data.getContent();
                if(media.Current == 0)//??
                    mAdapter.AppendToHistory(data);
                else
                    mAdapter.UpdateItem(data, (int)data.getTimestamp() - 1);
            }
            else
                mAdapter.AppendToHistory(data);
        }
    }

    private void Send(final String data) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message(SharedMemory.Username, mTopic, MessageType.Text, data);
                InetSocketAddress broker = SharedMemory.SelectBroker(mTopic);

                Tunnel tunnel = new Tunnel();
                tunnel.Connect(broker.getHostName(), broker.getPort());

                tunnel.Send(msg);
                tunnel.Close();
            }
        });
        th.start();
    }

    private void SetupUI() {
        Button buttonSend = findViewById(R.id.btnSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = mInputMessage.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MessageViewActivity.this, "Cannot send empty message.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Send(input);
            }
        });

        ImageView photo = findViewById(R.id.imgPicture);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedMemory.Upload = null;
                Intent intent = new Intent(MessageViewActivity.this, FilePickerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        ImageView camera = findViewById(R.id.imgVideo);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedMemory.Upload = null;
                Intent intent = new Intent(MessageViewActivity.this, FilePickerActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public void onActivityResult(int reqcode, int respcode, Intent data) {
        super.onActivityResult(reqcode, respcode, data);
        if (SharedMemory.Upload == null)
            return;
        if (reqcode == 1 && respcode == RESULT_OK)
            SendPhoto();
        else if (reqcode == 2 && respcode == RESULT_OK)
            SendVideo();

    }

    private void SendPhoto() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress broker = SharedMemory.SelectBroker(mTopic);
                Tunnel tunnel = new Tunnel();
                tunnel.Connect(broker.getHostName(), broker.getPort());

                FileReader reader = new FileReader(SharedMemory.Upload);
                final int chuncks = reader.GetFileSize() / Message.MiB + (reader.GetFileSize() % Message.MiB != 0 ? 1 : 0);
                final UUID id = UUID.randomUUID();
                int left = reader.GetFileSize();
                int current = 0;
                Message msg;
                while (left > 0) {
                    final int size = Math.min(Message.MiB, left);
                    byte[] data = reader.Read(size);
                    Media photo = new Media(id, current, chuncks, reader.GetFileSize(), data, ".jpg");
                    msg = new Message(SharedMemory.Username, mTopic, MessageType.Photo, photo);
                    tunnel.Send(msg);
                    left -= size;
                    current++;
                }
                tunnel.Close();
                reader.Close();
            }
        });
        thread.start();
    }

    private void SendVideo()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress broker = SharedMemory.SelectBroker(mTopic);
                Tunnel tunnel = new Tunnel();
                tunnel.Connect(broker.getHostName(), broker.getPort());

                FileReader reader = new FileReader(SharedMemory.Upload);
                final int chuncks = reader.GetFileSize() / Message.MiB + (reader.GetFileSize() % Message.MiB != 0 ? 1 : 0);
                final UUID id = UUID.randomUUID();
                int left = reader.GetFileSize();
                int current = 0;
                Message msg;
                while (left > 0) {
                    final int size = Math.min(Message.MiB, left);
                    byte[] data = reader.Read(size);
                    Media video = new Media(id, current, chuncks, reader.GetFileSize(), data, ".mp4");
                    msg = new Message(SharedMemory.Username, mTopic, MessageType.Video, video);
                    tunnel.Send(msg);
                    left -= size;
                    current++;
                }
                tunnel.Close();
                reader.Close();
            }
        });
        thread.start();
    }


    @Override
    protected void onDestroy()
    {
        mThread.Terminate();
        super.onDestroy();
    }
}