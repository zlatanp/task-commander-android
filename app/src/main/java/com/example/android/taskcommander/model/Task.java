package com.example.android.taskcommander.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tea on 3/31/2018.
 */

public class Task implements Serializable{
    @JsonProperty("id")
    private Long uid;
    @JsonProperty("name")
    private String caption;

    private String description;
    private Timestamp deadline;
    @JsonProperty("completed")
    private boolean isComplete = false;

    //foreign keys
    private int creator_uid;
    private Long group_id;
    private String assigneeMail;

    private double latitude;
    private double longitude;

    public Task() {

    }

    public Task(Long uid, String caption, String description, Timestamp deadline, boolean isComplete, int creator_uid, String assignee) {
        this.uid = uid;
        this.caption = caption;
        this.description = description;
        this.deadline = deadline;
        this.isComplete = isComplete;
        this.creator_uid = creator_uid;
        this.assigneeMail = assignee;
    }

    public Task(String caption, String description, Timestamp deadline) {
        this.caption = caption;
        this.description = description;
        this.deadline = deadline;
    }

    public Task(String caption, String description, Timestamp deadline, Long group_id, String assigneeMail) {
        this.caption = caption;
        this.description = description;
        this.deadline = deadline;
        this.group_id = group_id;
        this.assigneeMail = assigneeMail;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }
    public int getCreator_uid() {
        return creator_uid;
    }

    public void setCreator_uid(int creator_uid) {
        this.creator_uid = creator_uid;
    }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAssigneeMail() { return assigneeMail; }

    public void setAssigneeMail(String assigneeMail) {
        this.assigneeMail = assigneeMail;
    }
}
