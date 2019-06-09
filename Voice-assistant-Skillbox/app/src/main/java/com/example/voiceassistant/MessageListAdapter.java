package com.example.voiceassistant;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Creates viewHolder with corresponding message and displays it.
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    // List of all messages.
    public List<Message> messageList = new ArrayList<>();

    // Constants for answer / question message.
    private static final int ASSISTANT_TYPE = 0;
    private static final int USER_TYPE = 1;

    // This method calls every time when our message appears.
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int index) {
        Message message = messageList.get(index);

        ((MessageViewHolder)viewHolder).bind(message);
    }

    public int getItemViewType(int index) {
        Message message = messageList.get(index);

        // If it is sent by user, assign corresponding constant.
        if (message.isSent) {
            return USER_TYPE;
        } else {
            return ASSISTANT_TYPE;
        }
    }

    /**
     * Creates a new view holder when there are no existing view holders
     * which the RecyclerView can reuse.
     *
     * So, for instance, if my RecyclerView can display 6 items at a time,
     * it will create 6-7 ViewHolders, and then automatically reuse them, each
     * time calling onBindViewHolder.
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType == USER_TYPE) {
            view = inflater.inflate(R.layout.user_message, viewGroup, false);
        } else {
            view = inflater.inflate(R.layout.assistant_message, viewGroup, false);
        }

        return new MessageViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
