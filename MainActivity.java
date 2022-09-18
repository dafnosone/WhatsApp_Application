package com.example.dschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dschat.fileutils.FileReader;
import com.example.dschat.structs.Topic;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    //UI items
    EditText mInput;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInput = findViewById(R.id.textViewUsername);
        mButton = findViewById(R.id.btnConnect);
        SharedMemory.AppPath = getExternalFilesDir(null).getPath();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = mInput.getText().toString();
                if(input.length() == 0)
                {
                    Toast.makeText(MainActivity.this, "You must specify a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedMemory.Username = input;
                makeFolders();
                connect();
            }
        });
    }

    private void makeFolders()
    {
        File file = new File(getExternalFilesDir(null), SharedMemory.Username+"/media");
        if(!file.exists())
            file.mkdirs();
    }

    private void connect()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run()
            {
                SharedMemory.Topics.clear();
                File folder = new File(getExternalFilesDir(null), SharedMemory.Username);
                for (final File fileEntry:folder.listFiles())
                {
                    if(fileEntry.getName().compareTo("media") == 0)
                        continue;
                    Topic topic = FileReader.ReadTopic(fileEntry.getPath());
                    SharedMemory.Topics.add(topic);
                }
                Intent intent = new Intent(MainActivity.this, SelectTopicActivity.class);
                startActivity(intent);
            }
        });
        t.start();

    }

}