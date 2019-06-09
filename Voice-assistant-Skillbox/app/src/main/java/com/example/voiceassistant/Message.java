package com.example.voiceassistant;

import java.util.Date;


/**
 * Data for one message.
 */
public class Message {
    public String text;
    public Date date;

    // true - sent by user, false - assistant answer.
    public Boolean isSent;

    public Message(String text, Boolean isSent) {
        this.text = text;
        this.date = new Date();
        this.isSent = isSent;
    }
}
