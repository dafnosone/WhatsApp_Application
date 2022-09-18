package com.example.dschat.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dschat.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageViewHolder extends RecyclerView.ViewHolder{

    private TextView labelSender, labelMessage, labelTimestamp;
    private CardView msgCardView;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        labelSender = itemView.findViewById(R.id.labelSender);
        labelMessage = itemView.findViewById(R.id.labelMessage);
        labelTimestamp = itemView.findViewById(R.id.labelTimestamp);
        msgCardView = itemView.findViewById(R.id.msgCardView);
    }

    public void setSender(String sender) { labelSender.setText(sender+":");}

    public void setLabelMessage(String content) {
        labelMessage.setText(content);
    }

    public void setLabelTimestamp(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        labelTimestamp.setText(formatter.format(date));
    }

    public CardView getMsgCardView() { return msgCardView; }

}