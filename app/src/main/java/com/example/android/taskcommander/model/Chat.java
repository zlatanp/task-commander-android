package com.example.android.taskcommander.model;

import java.util.ArrayList;

/**
 * Created by Tea on 3/31/2018.
 */

public class Chat {

    private int uid;

    //foreign keys
    private Group group;
    private ArrayList<Message> messages;

    public Chat(int uid, Group group, ArrayList<Message> messages) {
        this.uid = uid;
        this.group = group;
        this.messages = messages;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
