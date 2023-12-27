package com.stream.donalive.ui.chat.model;

import java.util.Date;

public class Message {
    private String senderId;
    private String receiverId;
    private String messageText;
    private Date timestamp;

    // Add constructors, getters, setters as needed


    public Message() {
    }

    public Message(String senderId, String receiverId, String messageText, Date timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

