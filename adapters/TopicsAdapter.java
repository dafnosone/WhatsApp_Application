package com.example.dschat.adapters;

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

import com.example.dschat.MessageViewActivity;
import com.example.dschat.R;
import com.example.dschat.structs.TopicsForAdapter;

import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> {

    private ArrayList<TopicsForAdapter> mTopics = new ArrayList<>();
    private Context mContext;

    public TopicsAdapter(Context context) { mContext = context; }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false);
        TopicViewHolder holder = new TopicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        TopicsForAdapter data = mTopics.get(position);
        holder.mTextView.setText(data.TopicName);
        holder.mImageViewNewMessage.setVisibility(data.NewMsg ? View.VISIBLE : View.GONE);
        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =  new Intent(mContext, MessageViewActivity.class);
                intent.putExtra("topic", mTopics.get(holder.getAdapterPosition()).TopicName);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return mTopics.size(); }

    public class TopicViewHolder extends RecyclerView.ViewHolder{
        private CardView mParent;
        private TextView mTextView;
        private ImageView mImageViewNewMessage;

        public TopicViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mParent = itemView.findViewById(R.id.parent);
            mTextView = itemView.findViewById(R.id.labelTopicName);
            mImageViewNewMessage = itemView.findViewById(R.id.imgNewMessage);
        }
    }

    public void setItems(ArrayList<TopicsForAdapter> data)
    {
        mTopics = data;
        notifyDataSetChanged();
    }
}
