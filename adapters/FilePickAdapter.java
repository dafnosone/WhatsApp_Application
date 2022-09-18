package com.example.dschat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dschat.FilePickerActivity;
import com.example.dschat.MessageViewActivity;
import com.example.dschat.R;
import com.example.dschat.SharedMemory;

import java.io.File;

public class FilePickAdapter extends RecyclerView.Adapter<FilePickAdapter.FileEntryViewHolder>{

    private File mFolder;
    private File[] mContents;
    private Activity mActivity;

    public FilePickAdapter(Activity mContext) {
        this.mActivity = mContext;
    }

    @NonNull
    @Override
    public FileEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filepick_item, parent, false);
        FileEntryViewHolder holder = new FileEntryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileEntryViewHolder holder, int position)
    {
        final File entry = mContents[position];
        holder.mFilename.setText(entry.getName());
        holder.mIcon.setImageResource(entry.isDirectory() ? R.drawable.icon_folder : R.drawable.icon_file);
        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entry.isDirectory())
                {
                    File newfolder = new File(mFolder, entry.getName());
                    ChangeDirectory(newfolder);
                }
                else
                {
                    SharedMemory.Upload = entry.getPath();
                    mActivity.setResult(Activity.RESULT_OK);
                    mActivity.finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return mContents != null ? mContents.length : 0; }

    public class FileEntryViewHolder extends RecyclerView.ViewHolder{
        private CardView mParent;
        private ImageView mIcon;
        private TextView mFilename;

        public FileEntryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mParent = itemView.findViewById(R.id.cardViewFilePick);
            mIcon = itemView.findViewById(R.id.imgFileEntryIcon);
            mFilename = itemView.findViewById(R.id.labelFilename);
        }
    }

    public void ChangeDirectory(File filepath)
    {
        mFolder = filepath;
        mContents = mFolder.listFiles();
        notifyDataSetChanged();
    }
}
