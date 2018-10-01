package com.example.android.taskcommander.util;

import android.content.Context;

import com.example.android.taskcommander.model.Group;
import com.example.android.taskcommander.model.Message;
import com.example.android.taskcommander.model.Task;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Tea on 5/2/2018.
 */

public class JsonToClassMapper {

    public ArrayList<Group> groupsMapping(JSONObject object, Context context) {

        ArrayList<Group> groups_objects = new ArrayList<>();
        try {
            JSONArray task_groups = object.getJSONArray("task_groups");
            Group group_object = null;

            for (int i = 0; i < task_groups.length(); i++) {
                JSONObject group = (JSONObject) task_groups.get(i);
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                group_object = mapper.readValue(group.toString(), Group.class);// read from json string

                ArrayList<String> members = new ArrayList<>();
                JSONArray json_members = group.getJSONArray("members");

                for(int j=0; j<json_members.length(); j++){
                    members.add(((JSONObject)json_members.get(j)).getString("email"));
                }

                group_object.setGroupMembers(members);
                groups_objects.add(group_object);
            }


        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return groups_objects;
    }

    public ArrayList<Task> tasksMapping(JSONArray tasks, Context context) {

        ArrayList<Task> tasks_objects = new ArrayList<>();
        try {

            Task task_object = null;

            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = (JSONObject) tasks.get(i);
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                task_object = mapper.readValue(task.toString(), Task.class);// read from json string
                task_object.setAssigneeMail(task.getJSONObject("assignee").getString("email"));
                tasks_objects.add(task_object);
            }


        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks_objects;
    }

    public ArrayList<Message> chatMapping(JSONArray messages, Context context) {

        ArrayList<Message> message_objects = new ArrayList<>();
        try {

            Message message_object = null;

            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = (JSONObject) messages.get(i);
                String user = message.getString("sender");
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                message_object = mapper.readValue(message.toString(), Message.class);// read from json string
                message_object.setSender(user);

                if(SessionHandler.loggedEmail().equals(message_object.getSender())){
                //if(message_object.getSender().equals("dad@mail.com")){
                    message_object.setMsgType("MSG_TYPE_SENT");
                }else{
                    message_object.setMsgType("MSG_TYPE_RECEIVED");
                }

                message_objects.add(message_object);
            }


        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(message_objects, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m1.getTimestamp().compareTo(m2.getTimestamp());
            }
        });

        return message_objects;
    }
}