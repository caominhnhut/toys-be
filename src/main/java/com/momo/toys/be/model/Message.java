package com.momo.toys.be.model;

import java.util.Calendar;

import com.momo.toys.be.enumeration.MessageType;

public class Message{

    private MessageType type;

    private String content;

    private String sender;

    private Calendar timestamp;

    private MessageType messageType;

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getSender(){
        return sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public Calendar getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp){
        this.timestamp = timestamp;
    }

    public MessageType getMessageType(){
        return messageType;
    }

    public void setMessageType(MessageType messageType){
        this.messageType = messageType;
    }

    @Override
    public String toString(){
        return String.join(" - ", sender, content, messageType.toString(), timestamp.toString());
    }
}
