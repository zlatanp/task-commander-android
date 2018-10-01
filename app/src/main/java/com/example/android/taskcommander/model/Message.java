package com.example.android.taskcommander.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Tea on 3/31/2018.
 */

public class Message {

    public final static String MSG_TYPE_SENT = "MSG_TYPE_SENT";

    public final static String MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED";

    @JsonProperty("id")
    private Long uid;

    @JsonProperty("timestamp")
    private Timestamp timestamp;

    @JsonProperty("text")
    private String content;

    @JsonProperty("msgType")
    private String msgType;

    private String sender; //email

    public Message() {
        super();
    }

    public Message(Long uid, Timestamp timestamp, String content) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.content = content;
    }

    public Message(String msgType, String content) {
        this.msgType = msgType;
        this.content = content;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() { return msgType; }

    public void setMsgType(String msgType) { this.msgType = msgType; }

    public String getSender() { return sender; }

    public void setSender(String sender) { this.sender = sender; }
}
