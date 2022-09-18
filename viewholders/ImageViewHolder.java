package com.example.dschat.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dschat.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageViewHolder extends RecyclerView.ViewHolder{

    private TextView labelSender, labelTimestamp;
    private ImageView image;
    private CardView msgCardView;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        labelSender = itemView.findViewById(R.id.labelImgSender);
        image = itemView.findViewById(R.id.labelImage);
        labelTimestamp = itemView.findViewById(R.id.labelImgTimestamp);
        msgCardView = itemView.findViewById(R.id.msgImgCardView);
    }

    public void setSender(String sender) { labelSender.setText(sender+":");}

    public void setLabelTimestamp(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        labelTimestamp.setText(formatter.format(date));
    }

    public CardView getMsgCardView() { return msgCardView; }

    public ImageView getImage() { return image; }

}