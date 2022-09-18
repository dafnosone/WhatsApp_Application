package com.example.dschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.dschat.adapters.FilePickAdapter;

import java.io.File;

public class FilePickerActivity extends AppCompatActivity {

    private RecyclerView mRecView;
    private FilePickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        mRecView = findViewById(R.id.recViewFilePick);
        adapter = new FilePickAdapter(this);
        File folder = new File("/storage/emulated/0/DCIM/");//??
        if(folder.exists() && folder.isDirectory())
            Log.d("files", folder.getAbsolutePath()+":"+folder.listFiles().length);
        else
            Log.d("files", "Doesn't extists");
        adapter.ChangeDirectory(folder);
        mRecView.setAdapter(adapter);
        mRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}