package com.stream.prettylive.ui.chat.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stream.prettylive.R;
import com.stream.prettylive.ui.chat.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int CHAT_END = 1;
    private static final int CHAT_START = 2;

    private List<Message> mDataSet=new ArrayList<>();
    private String mId;
    public ChatAdapter(List<Message> dataSet, String id) {
        mDataSet = dataSet;
        mId = id;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_END) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_end, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_start, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mDataSet.get(position);
        if (message != null) {
            String senderId = message.getSenderId();
            String receiverId = message.getReceiverId();

            if (senderId != null && receiverId != null) {
                // Check if the message sender ID matches the current user's ID (mId)
                if (senderId.equals(mId)) {
                    return CHAT_END; // Current user's message
                } else if (receiverId.equals(mId)) {
                    return CHAT_START; // Message from the other user (receiver)
                }
            }
        }

        // Default view type if IDs are not set or don't match
        return CHAT_START;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDataSet != null && position < mDataSet.size()) {
            Message chat = mDataSet.get(position);
            if (chat != null) {
                holder.mTextView.setText(chat.getMessageText());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.tvMessage);
        }
    }
}
