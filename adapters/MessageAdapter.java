package com.example.dschat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.example.dschat.R;
import com.example.dschat.SharedMemory;
import com.example.dschat.netutils.Message;
import com.example.dschat.netutils.MessageType;
import com.example.dschat.structs.Media;
import com.example.dschat.viewholders.ImageViewHolder;
import com.example.dschat.viewholders.MessageViewHolder;
import com.example.dschat.viewholders.VideoViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int TEXT_MESSAGE = 0;
    private static final int IMAGE_MESSAGE = 1;
    private static final int VIDEO_MESSAGE = 2;

    private ArrayList<Message> mHistory = new ArrayList<>();
    private Context mContext;

    public MessageAdapter(Context context) { mContext = context; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        RecyclerView.ViewHolder holder;
        switch (viewType)
        {
            case TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                holder = new MessageViewHolder(view);
                return holder;
            case IMAGE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
                holder = new ImageViewHolder(view);
                return holder;
            case VIDEO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
                holder = new VideoViewHolder(view);
                return holder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Message msg = mHistory.get(position);
        switch (msg.getType()) {
            case Text:
            {
                MessageViewHolder messageViewHolder = (MessageViewHolder)holder;
                messageViewHolder.setSender(msg.getUsername());
                messageViewHolder.setLabelMessage((String)msg.getContent());
                messageViewHolder.setLabelTimestamp(msg.getDate());
                break;
            }
            case Photo:
            {
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.setSender(msg.getUsername());
                imageViewHolder.setLabelTimestamp(msg.getDate());
                Media image = (Media) msg.getContent();
                File buffer = new File(mContext.getExternalFilesDir(null), SharedMemory.Username + "/media/" + image.ID + image.Extension);
                Glide.with(mContext).load(buffer).signature(new ObjectKey(image.Current)).into(imageViewHolder.getImage());
                break;
            }
            case Video:
            {
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                videoViewHolder.setSender(msg.getUsername());
                videoViewHolder.setLabelTimestamp(msg.getDate());
                Media video = (Media) msg.getContent();
                File buffer = new File(mContext.getExternalFilesDir(null), SharedMemory.Username + "/media/" + video.ID + video.Extension);
                MediaController controller = new MediaController(mContext);
                videoViewHolder.getVideo().setVideoPath(buffer.getPath());
                videoViewHolder.getVideo().setMediaController(controller);
                if(video.Current != 0)
                {
                    videoViewHolder.getVideo().start();
                    videoViewHolder.setLoaded(true);
                    videoViewHolder.getVideo().pause();
                }
                else
                    videoViewHolder.setLoaded(false);
            }
            default:
                break;
        }

    }


    @Override
    public int getItemCount() { return mHistory.size(); }

    public void setHistory(ArrayList<Message> history)
    {
        mHistory = history;
        notifyDataSetChanged();
    }

    public void AppendToHistory(Message msg)
    {
        mHistory.add(msg);
        notifyItemChanged(mHistory.size() - 1);
    }

    public void UpdateItem(Message msg, int index)
    {
        if(msg.getType() == MessageType.Photo || msg.getType() == MessageType.Video)
        {
            Media old = (Media) mHistory.get(index).getContent();
            Media newdata = (Media) msg.getContent();
            old.Current = newdata.Current;
        }
        notifyItemChanged(index);
    }

    @Override
    public int getItemViewType(int position)
    {
        switch (mHistory.get(position).getType())
        {
            case Text:  return TEXT_MESSAGE;
            case Photo: return IMAGE_MESSAGE;
            case Video: return VIDEO_MESSAGE;
            default:    return -1;
        }
    }
}
