package com.example.android.taskcommander.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.GroupsAdapter;
import com.example.android.taskcommander.adapters.MembersAdapter;
import com.example.android.taskcommander.model.Group;
import com.example.android.taskcommander.util.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.android.taskcommander.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewGroupActivity extends AppCompatActivity {
    private ArrayList<String> members = new ArrayList<String>();
    private RecyclerView recyclerView;
    private EditText new_group_member_et;
    private MembersAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        new_group_member_et = findViewById(R.id.new_group_member_et);

    }

    public void onAddMemberButtonClicked(final View view) {
        final Context context = view.getContext();

        members.add(new_group_member_et.getText().toString());

        recyclerView = (RecyclerView) findViewById(R.id.members_recycler_view);
        mAdapter = new MembersAdapter(context, members);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        new_group_member_et.setText("");
    }

    public void onCreateGroupButtonClicked(final View view) {

        final Context context = view.getContext();
        final Intent intent = new Intent(context, GroupsActivity.class);

        EditText nameField = (EditText) findViewById(R.id.new_group_name_et);

        String name = nameField.getText().toString();
        ArrayList<String> members = new ArrayList<>();
        members.add(SessionHandler.loggedEmail());
        members.addAll(this.members);
        final Group newGroup = new Group(name, members);

        AndroidNetworking.initialize(this);
        ObjectMapper mapper = new ObjectMapper();
        //Object to JSON in file
        try {
            String jsonInString = mapper.writeValueAsString(newGroup);
            String array = mapper.writeValueAsString(members);
            JSONArray array_obj = new JSONArray(array);
            JSONObject obj = new JSONObject(jsonInString);
            obj.putOpt("members", array_obj);
            AndroidNetworking.post(HttpUtils.WEB_SERVICE_BASE+"/task_group/create")
                    .addJSONObjectBody(obj)
                    .addBodyParameter("name", newGroup.getName()) // posting java object
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            intent.putExtra("newGroup", newGroup);
                            context.startActivity(intent);
                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
