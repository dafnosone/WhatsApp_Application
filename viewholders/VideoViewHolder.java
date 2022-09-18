package com.example.dschat.viewholders;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dschat.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    private TextView labelSender, labelTimestamp;
    private VideoView video;
    private CardView msgCardView;
    private ProgressBar progressBar;

    public VideoViewHolder(@NonNull View itemView)
    {
        super(itemView);

        labelSender = itemView.findViewById(R.id.labelVideoSender);
        video = itemView.findViewById(R.id.labelVideo);
        labelTimestamp = itemView.findViewById(R.id.labelVideoTimestamp);
        progressBar = itemView.findViewById(R.id.progressBarLoaded);
        msgCardView = itemView.findViewById(R.id.cardViewForVideo);

    }

    public void setSender(String sender) { labelSender.setText(sender+":");}

    public void setLabelTimestamp(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        labelTimestamp.setText(formatter.format(date));
    }

    public CardView getMsgCardView() { return msgCardView; }

    public VideoView getVideo() { return video; }

    public void setLoaded(boolean flag) { progressBar.setVisibility( flag ? View.INVISIBLE : View.VISIBLE);}
}
