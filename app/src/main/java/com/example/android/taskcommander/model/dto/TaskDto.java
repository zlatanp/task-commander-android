package com.example.android.taskcommander.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Tea on 5/20/2018.
 */

public class TaskDto implements Serializable{

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

    public TaskDto() {

        }

        public TaskDto(String caption, String description, Timestamp deadline, boolean isComplete, int creator_uid, String assignee) {

            this.caption = caption;
            this.description = description;
            this.deadline = deadline;
            this.isComplete = isComplete;
            this.creator_uid = creator_uid;
            this.assigneeMail = assignee;
        }

        public TaskDto(String caption, String description, Timestamp deadline) {
            this.caption = caption;
            this.description = description;
            this.deadline = deadline;
        }

        public TaskDto(String caption, String description, Timestamp deadline, Long group_id, String assigneeMail) {
            this.caption = caption;
            this.description = description;
            this.deadline = deadline;
            this.group_id = group_id;
            this.assigneeMail = assigneeMail;

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

        public String getAssigneeMail() { return assigneeMail; }

        public void setAssigneeMail(String assigneeMail) {
            this.assigneeMail = assigneeMail;
        }
}
