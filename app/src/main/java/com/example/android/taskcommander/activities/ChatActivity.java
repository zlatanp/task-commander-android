package com.example.android.taskcommander.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.ChatMsgAdapter;
import com.example.android.taskcommander.model.Group;
import com.example.android.taskcommander.model.Message;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.example.android.taskcommander.util.SessionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private Group group;
    private ArrayList<Message> messages = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle("Android Chat App Example");
        Intent intent = getIntent();

        group = (Group)intent.getSerializableExtra("parentGroup");

        AndroidNetworking.initialize(this);
        //AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/user/initialRequest/"+ SessionHandler.loggedEmail())
        AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/message/get_all/"+group.getUid())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        JsonToClassMapper jsonToClassMapper = new JsonToClassMapper();
                        messages =  jsonToClassMapper.chatMapping(response, getApplicationContext());

                        msgRecyclerView = (RecyclerView)findViewById(R.id.chat_recycler_view);
                        // Set RecyclerView layout manager.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        msgRecyclerView.setLayoutManager(linearLayoutManager);


                        // Create the data adapter with above data list.
                        final ChatMsgAdapter chatMsgAdapter = new ChatMsgAdapter(messages);

                        // Set data adapter to RecyclerView.
                        msgRecyclerView.setAdapter(chatMsgAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        msgRecyclerView.setLayoutManager(llm);

                        final EditText msgInputText = (EditText)findViewById(R.id.chat_input_msg);

                        Button msgSendButton = (Button)findViewById(R.id.chat_send_msg);

                        msgSendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String msgContent = msgInputText.getText().toString();
                                if(!TextUtils.isEmpty(msgContent))
                                {

                                    // Add a new sent message to the list.
                                    Message msgDto = new Message(Message.MSG_TYPE_SENT, msgContent);
                                    //msgDto.setSender(SessionHandler.loggedEmail());
                                    msgDto.setSender(SessionHandler.loggedEmail());
                                    messages.add(msgDto);

                                    AndroidNetworking.initialize(getApplicationContext());
                                    ObjectMapper mapper = new ObjectMapper();
                                    //Object to JSON in file
                                    try {
                                        String jsonInString = mapper.writeValueAsString(msgDto);
                                        JSONObject obj = new JSONObject(jsonInString);

                                        AndroidNetworking.post(HttpUtils.WEB_SERVICE_BASE+"/message/create/"+group.getUid())
                                                .addJSONObjectBody(obj)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        int newMsgPosition = messages.size() - 1;

                                                        // Notify recycler view insert one new data.
                                                        chatMsgAdapter.notifyItemInserted(newMsgPosition);

                                                        // Scroll RecyclerView to the last message.
                                                        msgRecyclerView.scrollToPosition(newMsgPosition);

                                                        // Empty the input edit text box.
                                                        msgInputText.setText("");
                                                    }
                                                    @Override
                                                    public void onError(ANError error) {
                                                        // handle error
                                                        System.out.print("");
                                                    }
                                                });
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println("onError");
                    }
                });


    }
}