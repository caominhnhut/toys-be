package com.momo.toys.be.dto;

import java.util.Calendar;

public class PrivateMessage{

    private String userOneName;

    private String userTwoName;

    private String content;

    private Calendar timestamp;

    public String getUserOneName(){
        return userOneName;
    }

    public void setUserOneName(String userOneName){
        this.userOneName = userOneName;
    }

    public String getUserTwoName(){
        return userTwoName;
    }

    public void setUserTwoName(String userTwoName){
        this.userTwoName = userTwoName;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public Calendar getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp){
        this.timestamp = timestamp;
    }
}
