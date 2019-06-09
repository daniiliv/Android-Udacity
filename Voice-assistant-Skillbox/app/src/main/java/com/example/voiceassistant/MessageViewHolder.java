package com.example.voiceassistant;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Соединяет данные с xml.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    protected TextView messageText;
    protected TextView messageTime;

    public MessageViewHolder(View item) {
        super(item);
        messageText = item.findViewById(R.id.messageTextView);
        messageTime = item.findViewById(R.id.messageTimeView);
    }

    public void bind(Message message) {
        messageText.setText(message.text);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        messageTime.setText(dateFormat.format(message.date));
    }
}
