package com.example.android.taskcommander.model;

/**
 * Created by Tea on 3/31/2018.
 */

public class Member {

    private User user;
    private Group group;

    public Member(User user, Group group) {
        this.user = user;
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
